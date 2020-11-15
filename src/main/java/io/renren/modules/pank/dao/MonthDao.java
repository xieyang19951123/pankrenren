package io.renren.modules.pank.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.pank.entity.Month;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MonthDao extends BaseMapper<Month> {

    List<Month> yearsnumbers();

    List<Month> monthnumbers(int years);

    List<Month> weeknumbers(@Param("years") int years, @Param("month") int month);
}
