package io.renren.modules.pank.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.pank.entity.Dot;
import io.renren.modules.pank.entity.ShowEntity;

import java.util.List;
import java.util.Map;

public interface DotService extends IService<Dot> {
    PageUtils getlist(Map<String, Object> params);

    List<ShowEntity> getPunchup();

    R getAll();
}
