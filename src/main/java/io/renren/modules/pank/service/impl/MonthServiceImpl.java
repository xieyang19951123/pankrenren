package io.renren.modules.pank.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.R;
import io.renren.modules.pank.dao.MonthDao;
import io.renren.modules.pank.entity.Month;
import io.renren.modules.pank.service.MonthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class MonthServiceImpl extends ServiceImpl<MonthDao, Month> implements MonthService {

    @Autowired
    private MonthDao monthDao;
    @Override
    public R getMonthWeek() {
        Calendar instance = Calendar.getInstance();
        int years = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH )+ 1;
        List<Month> monthnumbers = monthDao.monthnumbers(years);
        List<Month> yearsnumbers = monthDao.yearsnumbers();
        System.out.println(years+" ="+ month);
        List<Month> weeknumbers = monthDao.weeknumbers(years, month);

        return R.ok().put("monthnumbers",monthnumbers)
                .put("yearsnumbers",yearsnumbers)
                .put("weeknumbers",weeknumbers)
                .put("month",month)
                .put("years",years);
    }


}
