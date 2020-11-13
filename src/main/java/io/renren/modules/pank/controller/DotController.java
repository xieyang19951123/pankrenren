package io.renren.modules.pank.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.idworker.Sid;
import io.renren.modules.pank.dao.DotDao;
import io.renren.modules.pank.entity.Accessory;
import io.renren.modules.pank.entity.Dot;
import io.renren.modules.pank.service.DotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pank/dot")
public class DotController {

    @Autowired
    private DotService dotService;

    @Autowired
    private DotDao dotDao;


    @Autowired
    private Sid sid;

    @RequestMapping(value = "insetrDot",method = RequestMethod.POST)
    public  R insetrDot(@RequestBody Dot dot){
        String nextShort = sid.nextShort();
        if(dot == null){
            return R.error("参数为空");
        }
        if(dot.getAids()!=null){
            dot.setAId(Arrays.asList(dot.getAids()).toString());
        }
        dot.setDTag(nextShort);
        dot.setShow_status(1);
        dotService.save(dot);
        return R.ok();
    }

    @RequestMapping(value = "editDot",method = RequestMethod.POST)
    public  R editDot(@RequestBody Dot dot){
        if(dot == null){
            return  R.error("参数错误");
        }
        if(dot.getAids()!=null){
            dot.setAId(Arrays.asList(dot.getAids()).toString());
        }
        dotService.updateById(dot);
        return R.ok();
    }

    @RequestMapping("getlist")
    public R getlist(@RequestParam Map<String,Object> params){
        PageUtils pageUtils = dotService.getlist(params);
        return R.ok().put("page",pageUtils);
    }

    @RequestMapping(value = "deleted",method = RequestMethod.POST)
    public  R deleted(@RequestBody Integer[] ids ){
        dotService.getBaseMapper().deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @GetMapping("info/{id}")
    public  R info(@PathVariable("id")Integer id){
        if(id == null){
            return R.error("参数为空");
        }

        Dot byId = dotService.getById(id);
        System.out.println(byId);
        if(byId!=null){
            String s = byId.getAId().replaceAll("\\[", "(").replaceAll("]", ")");
            List<Accessory> accessorys = dotDao.accessorys(s);
            byId.setAccessories(accessorys);
            return R.ok().put("page",byId);
        }
        return R.ok().put("page",dotService.getById(id));
    }

    @RequestMapping(value = "getPunchup",method = RequestMethod.GET)
    public  R getPunchup(){

        return R.ok().put("page",dotService.getPunchup());
    }

    @RequestMapping(value = "getAllDot",method = RequestMethod.GET)
    public R getAll(){

        return dotService.getAll();
    }
}
