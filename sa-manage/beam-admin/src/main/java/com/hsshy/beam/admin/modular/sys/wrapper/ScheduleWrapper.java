package com.hsshy.beam.admin.modular.sys.wrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hsshy.beam.admin.common.factory.impl.ConstantFactory;
import com.hsshy.beam.admin.common.quartz.ScheduleJob;
import com.hsshy.beam.web.modular.base.warpper.BaseControllerWrapper;

import java.util.Map;

public class ScheduleWrapper extends BaseControllerWrapper {


    public ScheduleWrapper(IPage<ScheduleJob> page) {
        super(page);
    }


    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("statusName", ConstantFactory.me().getDictsByCode("schedule_status",map.get("status")+""));
    }



}
