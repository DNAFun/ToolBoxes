package com.xmingl.workcenter.service;

import com.xmingl.base.anno.AutoConfModule;
import com.xmingl.base.data.DataUtil;
import com.xmingl.base.date.DateUtil;
import com.xmingl.base.service.BaseService;
import com.xmingl.workcenter.bean.EventInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@AutoConfModule
public class WorkCenterServiceImpl extends BaseService implements WorkCenterService {
    public static Logger logger = LoggerFactory.getLogger(WorkCenterServiceImpl.class);

    @Override
    public List<EventInfo> listEventInfosByDate(Date date) throws IOException {
        logger.info("查看{}工作日志", DateUtil.format(date, DateUtil.YYYY_MM_DD_ZH));
        return DataUtil.loadDataList(BASE_FILE_PATH + DateUtil.format(date, DateUtil.shortFormat), EventInfo.class);
    }

    @Override
    public int listDateTaskCount(Date date) throws IOException {
        logger.info("获取{}工作数量", DateUtil.format(date, DateUtil.YYYY_MM_DD_ZH));
        return DataUtil.listDataNum(BASE_FILE_PATH + DateUtil.format(date, DateUtil.shortFormat));
    }

}
