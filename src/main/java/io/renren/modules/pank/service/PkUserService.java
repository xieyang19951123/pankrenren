package io.renren.modules.pank.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.pank.entity.PkUserEntity;

import java.util.Map;

/**
 * 用户管理
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-26 22:42:52
 */
public interface PkUserService extends IService<PkUserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R rankinglist();

    R deletePaiming();
}

