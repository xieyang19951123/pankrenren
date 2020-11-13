package io.renren.modules.pank.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.pank.entity.Accessory;
import io.renren.modules.pank.entity.Dot;
import io.renren.modules.pank.entity.DotVo;
import io.renren.modules.pank.entity.ShowEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DotDao extends BaseMapper<Dot> {

    IPage<DotVo> getList(Page<?> page);

    List<Accessory> accessorys(String ids);

    List<ShowEntity> getPunchup();
}
