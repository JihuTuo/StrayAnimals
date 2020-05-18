package com.hsshy.beam.admin.modular.sys.wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hsshy.beam.admin.common.factory.impl.ConstantFactory;
import com.hsshy.beam.web.modular.base.warpper.BaseControllerWrapper;
import java.util.List;
import java.util.Map;

public class MenuWrapper extends BaseControllerWrapper {

    public MenuWrapper(List<Map> list) {

        super(list);
    }



    public MenuWrapper(IPage<Map> page) {
        super(page);
    }


    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("typeName", ConstantFactory.me().getDictsByCode("menu_type", map.get("type")+""));
    }



}
