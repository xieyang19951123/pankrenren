package io.renren.modules.pank.dao;

import io.renren.modules.pank.entity.PkUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.pank.entity.Ranking;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户管理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-08-26 22:42:52
 */
@Mapper
public interface PkUserDao extends BaseMapper<PkUserEntity> {
    List<Ranking> selectpangpna();
}
