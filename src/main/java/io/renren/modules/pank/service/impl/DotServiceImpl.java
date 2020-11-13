package io.renren.modules.pank.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiniu.sms.model.TemplateInfo;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.pank.dao.DotDao;
import io.renren.modules.pank.dao.PkUserDao;
import io.renren.modules.pank.entity.*;
import io.renren.modules.pank.service.DotService;
import io.renren.modules.pank.service.PkUserService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DotServiceImpl extends ServiceImpl<DotDao, Dot> implements DotService {

    @Autowired
    private DotDao dotDao;

    @Autowired
    private PkUserDao pkUserDao;


    @Override
    public PageUtils getlist(Map<String, Object> params) {
        IPage<Dot> page = this.page(
                new Query<Dot>().getPage(params),
                new QueryWrapper<Dot>()
        );
        List<Dot> collect = page.getRecords().stream().map(item -> {
            if(StringUtils.isEmpty(item.getAId())){
                return item;
            }
            String s = item.getAId().replaceAll("\\[", "(").replaceAll("]", ")");
            List<Accessory> accessorys = dotDao.accessorys(s);
            item.setAccessories(accessorys);
            return item;
        }).collect(Collectors.toList());
        page.setRecords(collect);
        //IPage<DotVo> list = dotDao.getList(new Query<DotVo>().getMyPage(params));
        return new PageUtils(page);
    }

    @Override
    public List<ShowEntity> getPunchup() {

       return dotDao.getPunchup();
    }

    @Override
    public R getAll() {
        List<PkUserEntity> pkUserEntities = pkUserDao.selectList(null);
//        Map<Object, List<PkUserEntity>> collect = pkUserEntities.stream().collect(Collectors.groupingBy(item ->
//            DateFormatUtils.format(item.getPkUserTime(), "yyyy-MM"))
//        ));
        System.out.println(pkUserEntities);
        Map<String, List<PkUserEntity>> collect = pkUserEntities.stream().collect(Collectors.groupingBy(item -> DateFormatUtils.format(item.getCreateTime(), "yyyy-MM-dd")));
        System.out.println(collect);
        ArrayList<String> keys = new ArrayList<>();
        List<Integer> vals = new ArrayList();
        collect.forEach((key,val)->{
            vals.add(val.size());
            keys.add(key);
        });
        ShowEntity showEntity = new ShowEntity();
        showEntity.setVals(vals);
        showEntity.setKeys(keys);
        return R.ok().put("page",showEntity);
    }
}
