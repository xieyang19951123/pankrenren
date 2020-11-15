package io.renren.modules.pank.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.R;
import io.renren.modules.pank.entity.Month;

public interface MonthService extends IService<Month> {

    R getMonthWeek();


}
