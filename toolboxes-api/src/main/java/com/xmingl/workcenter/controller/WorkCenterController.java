package com.xmingl.workcenter.controller;

import com.xmingl.base.date.DateUtil;
import com.xmingl.workcenter.bean.EventInfo;
import com.xmingl.workcenter.service.WorkCenterService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/WorkCenter")
public class WorkCenterController {
    @Resource
    private WorkCenterService workCenterService;

    @RequestMapping("listByDates")
    public List<EventInfo> listEventInfosByDates(long millsecord) throws IOException {
        Date date = DateUtil.getDate(millsecord);
        return workCenterService.listEventInfosByDate(date);
    }

    @RequestMapping("listDateTaskCount")
    public int listDateTaskCount(long millsecord) throws IOException {
        Date date = DateUtil.getDate(millsecord);
        return workCenterService.listDateTaskCount(date);
    }
}
