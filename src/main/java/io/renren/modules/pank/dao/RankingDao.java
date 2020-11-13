package io.renren.modules.pank.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.pank.entity.Ranking;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RankingDao extends BaseMapper<Ranking> {
}
