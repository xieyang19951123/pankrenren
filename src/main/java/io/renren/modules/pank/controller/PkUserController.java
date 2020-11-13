package io.renren.modules.pank.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.pank.entity.PkUserEntity;
import io.renren.modules.pank.service.PkUserService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 用户管理
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-26 22:42:52
 */
@RestController
@RequestMapping("pank/pkuser")
public class PkUserController {
    @Autowired
    private PkUserService pkUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("pank:pkuser:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pkUserService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("pank:pkuser:info")
    public R info(@PathVariable("id") Long id){
		PkUserEntity pkUser = pkUserService.getById(id);

        return R.ok().put("pkUser", pkUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("pank:pkuser:save")
    public R save(@RequestBody PkUserEntity pkUser){
		pkUserService.save(pkUser);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("pank:pkuser:update")
    public R update(@RequestBody PkUserEntity pkUser){
		pkUserService.updateById(pkUser);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("pank:pkuser:delete")
    public R delete(@RequestBody Long[] ids){
		pkUserService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping("/rankinglist")
    public R getlinking(){
       return pkUserService.rankinglist();
    }

    //清除排名
    @RequestMapping("/deletePaiming")
    public R deletePaiming(){
        return pkUserService.deletePaiming();
    }



}
