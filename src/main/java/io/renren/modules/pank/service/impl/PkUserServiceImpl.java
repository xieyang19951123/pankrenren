package io.renren.modules.pank.service.impl;

import io.renren.common.utils.R;
import io.renren.modules.app.dao.UserDao;
import io.renren.modules.pank.dao.DotDao;
import io.renren.modules.pank.dao.PunchCardRecordDao;
import io.renren.modules.pank.dao.RankingDao;
import io.renren.modules.pank.entity.*;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.pank.dao.PkUserDao;
import io.renren.modules.pank.service.PkUserService;


@Service("pkUserService")
public class PkUserServiceImpl extends ServiceImpl<PkUserDao, PkUserEntity> implements PkUserService {

    long nd = 1000 * 24 * 60 * 60;

    long nh = 1000 * 60 * 60;

    long nm = 1000 * 60;

    long ns = 1000;
    @Autowired
    private DotDao dotDao;

    @Autowired
    private PkUserDao pkUserDao;

    @Autowired
    private RankingDao rankingDao;
    @Autowired
    private PunchCardRecordDao punchCardRecordDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PkUserEntity> page = this.page(
                new Query<PkUserEntity>().getPage(params),
                new QueryWrapper<PkUserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public R rankinglist() {
        List<Ranking> selectpangpna = pkUserDao.selectpangpna();
        ArrayList<ShowEntity1> showEntities = new ArrayList<>();
        String mingci = null;

        //List<Ranking> collect1 = selectpangpna.stream().sorted(Comparator.comparing(Ranking::getUseTime)).collect(Collectors.toList());
        for (int i = 0; i <selectpangpna.size() ; i++) {

            List<Ranking> ranking = rankingDao.selectList(new QueryWrapper<>(selectpangpna.get(i)));
            Optional<Ranking> rs = ranking.stream().filter(item -> item.getUseTime() != null).distinct().min((e1, e2) -> e1.getUseTime().compareTo(e2.getUseTime()));
            if(!rs.isPresent()){
                continue;
            }
            Ranking ranking1 = rs.get();
            ShowEntity1 getshowentity = getshowentity(ranking1, ranking1.getTerminus());
            showEntities.add(getshowentity);
        }
        List<ShowEntity1> collect = showEntities.stream().sorted(Comparator.comparing(ShowEntity1::getSecond)).collect(Collectors.toList());
        return R.ok().put("page",collect);
    }

    @Override
    public R deletePaiming() {
        Ranking ranking = new Ranking();
        ranking.setComplete(1);
        ranking.setTeamId(0);
        List<Ranking> rankings = rankingDao.selectList(new QueryWrapper<>(ranking));
        List<Integer> arrayList = new ArrayList<Integer>();
        rankings.forEach(item->{
            arrayList.add(item.getId());
        });
        System.out.println(arrayList);
        rankingDao.deleteBatchIds(arrayList);
        return R.ok();
    }

    public ShowEntity1 getshowentity(Ranking ranking1, String tag){
        Dot current = new Dot();
        current.setDTag(tag);

        //当前点位的信息
        Dot currentDot = dotDao.selectOne(new QueryWrapper<>(current));
        System.out.println(currentDot);
        String[] currentlongitudes = currentDot.getLongitude().split(",");
        double  currentlongitude= Double.parseDouble(currentlongitudes[0]);//经度
        double  currentimensionality= Double.parseDouble(currentlongitudes[1]);//维度
        GlobalCoordinates startglobalCoordinates = new GlobalCoordinates( currentimensionality,currentlongitude);

        //起始点位
        Dot start = new Dot();
        start.setDTag(ranking1.getOrigin());

        Dot startDot = dotDao.selectOne(new QueryWrapper<>(start));

        String[] longitudes = startDot.getLongitude().split(",");
        double  startlongitude= Double.parseDouble(longitudes[0]);//经度
        double  stardimensionality= Double.parseDouble(longitudes[1]);//维度
        GlobalCoordinates currentglobalCoordinates = new GlobalCoordinates(stardimensionality, startlongitude);


        double meter1 = getDistanceMeter(startglobalCoordinates, currentglobalCoordinates, Ellipsoid.Sphere)/1000;//距离
        //当前时间
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(new Date());
        //秒钟差
        long sec = 0;
        if(ranking1.getStartTime() == null){
            sec =0;
        }else{
            if(ranking1.getComplete() == 1){
                sec = ranking1.getEndTime().getTime()- ranking1.getStartTime().getTime();
            }else {

                sec = (new Date().getTime() - ranking1.getStartTime().getTime());
            }
        }

        double mun =sec / nm;//分钟
        //计算卡路里//
        Random rand = new Random();
        //double d = rand.nextInt(3)+3.5;//随机生成卡路里的
        //生成消耗的卡路里
        double v = mun * 3.5;
        //查询打卡情况
        PunchCardRecord punchCardRecord = new PunchCardRecord();
        punchCardRecord.setRankingid(ranking1.getId());
        Integer yetcount = punchCardRecordDao.selectCount(new QueryWrapper<>(punchCardRecord));//已打卡的数量
        Integer notcount = dotDao.selectCount(null)-yetcount;
        PkUserEntity pkUserEntity = pkUserDao.selectById(ranking1.getUserid());
        ShowEntity1 showEntity = new ShowEntity1();
        showEntity.setMintue((sec% nd/nh>=10? sec% nd/nh : "0"+sec% nd/nh)+":"+(sec% nd % nh / nm>=10?sec% nd % nh / nm:"0"+sec% nd % nh / nm)+":"+(sec% nd % nh % nm / ns>=10?sec% nd % nh % nm / ns:"0"+sec% nd % nh % nm / ns));

        punchCardRecord.setRankingid(ranking1.getId());
        List<PunchCardRecord> punchCardRecords = punchCardRecordDao.selectList(new QueryWrapper<>(punchCardRecord));
        double d = 0;
        for (int i = 0; i <punchCardRecords.size() ; i++) {
            if((punchCardRecords.size()>(i+1))){
                d+=computed(punchCardRecords.get(i).getDotTag(),punchCardRecords.get(i+1).getDotTag());
            }
        }


        System.out.println(mun);
        System.out.println(meter1+"==========================");
        int musm =(int) Math.ceil((sec/d));  // 分钟/公里
        String metu = (musm / nm>=10?musm  / nm:"0"+musm/ nm)+"‘"+(musm% nd % nh % nm / ns>=10?musm% nd % nh % nm / ns:"0"+musm% nd % nh % nm / ns)+"“";
        if(!ranking1.getOrigin().equals(tag)){
            showEntity.setMintuegongl(metu);
        }
        showEntity.setKaluli(v);
        showEntity.setNotcount(notcount);
        showEntity.setYetcount(yetcount);
        showEntity.setSecond(sec);
        showEntity.setHeadimg(pkUserEntity.getHeadimgurl());
        showEntity.setNickName(pkUserEntity.getNickname());
        showEntity.setGongli(d);
        showEntity.setUid(ranking1.getUserid());
        return  showEntity;
    }

    public  double getDistanceMeter(GlobalCoordinates gpsFrom, GlobalCoordinates gpsTo, Ellipsoid ellipsoid)
    {
        //创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(ellipsoid, gpsFrom, gpsTo);

        return geoCurve.getEllipsoidalDistance();
    }

    private double computed(String tag1, String tag2){
        //System.out.println(tag1+"========="+tag2);
        Dot getdot = getdot(tag1);
        Dot getdot1 = getdot(tag2);
        String[] currentlongitudes = getdot.getLongitude().split(",");
        double  currentlongitude= Double.parseDouble(currentlongitudes[0]);//经度
        double  currentimensionality= Double.parseDouble(currentlongitudes[1]);//维度
        GlobalCoordinates startglobalCoordinates = new GlobalCoordinates( currentimensionality,currentlongitude);
        String[] longitudes = getdot1.getLongitude().split(",");
        double  startlongitude= Double.parseDouble(longitudes[0]);//经度
        double  stardimensionality= Double.parseDouble(longitudes[1]);//维度
        GlobalCoordinates currentglobalCoordinates = new GlobalCoordinates(stardimensionality, startlongitude);

        double meter1 = getDistanceMeter(startglobalCoordinates, currentglobalCoordinates, Ellipsoid.Sphere)/1000;//距离
        //System.out.println(meter1);
        return meter1;

    }
    private Dot getdot(String tag){

        Dot dot = new Dot();
        dot.setDTag(tag);
        Dot dot1 = dotDao.selectOne(new QueryWrapper<>(dot));

        return dot1;
    }
}