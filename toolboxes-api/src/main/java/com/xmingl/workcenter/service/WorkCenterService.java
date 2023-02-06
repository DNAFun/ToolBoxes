package com.xmingl.workcenter.service;

import com.xmingl.workcenter.bean.EventInfo;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface WorkCenterService {

    /**
     * 获取某一天的所有事件
     * @param date
     * @return
     * @throws IOException
     */
    List<EventInfo> listEventInfosByDate(Date date) throws IOException;

    /**
     * 获取该日期的任务数量
     * @param date
     * @return
     */
    int listDateTaskCount(Date date) throws IOException;

    /**
     * 新增日期任务
     * @param info
     * @return
     */
    boolean addDataTask(EventInfo info);
}
