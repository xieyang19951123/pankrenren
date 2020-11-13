package io.renren.modules.pank.controller;

import io.renren.common.utils.R;
import io.renren.modules.pank.dao.BulletinimgDao;
import io.renren.modules.pank.dao.DotDao;
import io.renren.modules.pank.entity.BulletinText;
import io.renren.modules.pank.entity.Bulletinimg;
import io.renren.modules.pank.service.BulletinTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pank")
public class BulletinTextController {

    @Autowired
    private BulletinTextService bulletinTextService;

    @Autowired
    private BulletinimgDao bulletinimgDao;

    @Autowired
    private DotDao dotDao;


    @RequestMapping(value = "insert",method = RequestMethod.POST)
    public R inset(@RequestBody BulletinText bulletinText ){
        bulletinTextService.save(bulletinText);
        return R.ok();
    }


    @RequestMapping(value = "edit",method = RequestMethod.POST)
    public R edit(@RequestBody BulletinText bulletinText){
        bulletinTextService.updateById(bulletinText);
        return R.ok();
    }

    @RequestMapping(value = "deleted",method = RequestMethod.POST)
    public R deleted(@RequestBody Integer[] ids){


        bulletinTextService.getBaseMapper().deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping("getlist")
    public  R get(){
        List<BulletinText> list = bulletinTextService.list();
        return R.ok().put("page",list);
    }



    @RequestMapping(value = "editImg",method = RequestMethod.POST)
    private R editImg(@RequestBody Integer[] ids){
        Bulletinimg bulletinimg = new Bulletinimg();
        bulletinimg.setBulletinimg(Arrays.asList(ids).toString());
        bulletinimg.setId(1);
        bulletinimgDao.updateById(bulletinimg);

        return R.ok();
    }

    @RequestMapping(value = "getimglist",method = RequestMethod.GET)
    private R getimglist(){
        List<Bulletinimg> bulletinimgs = bulletinimgDao.selectList(null);
        List<Bulletinimg> collect = bulletinimgs.stream().map(item -> {
            String s = item.getBulletinimg().replaceAll("\\[", "(").replaceAll("]", ")");
            if(!s.equals("()")&&!s.equals("")){

                item.setAccessories(dotDao.accessorys(s));
            }
            return item;
        }).collect(Collectors.toList());
        return  R.ok().put("page",collect);
    }
}
