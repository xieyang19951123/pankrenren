package io.renren.modules.pank.controller;

import io.renren.common.utils.R;
import io.renren.modules.pank.service.MonthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pank/mongth")
public class MonthController {

    @Autowired
    private MonthService monthService;


    @RequestMapping("get")
    public R getmonth(){
       return monthService.getMonthWeek();
    }
}
