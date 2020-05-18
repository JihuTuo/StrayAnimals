package com.hsshy.beam.admin.modular.sys.wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hsshy.beam.admin.common.factory.impl.ConstantFactory;
import com.hsshy.beam.web.modular.base.warpper.BaseControllerWrapper;

import java.util.Map;

public class UserWrapper extends BaseControllerWrapper {



    public UserWrapper(IPage<Map> page) {
        super(page);
    }


    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("sexName", ConstantFactory.me().getDictsByCode("sex",map.get("sex")+""));
    }



}
