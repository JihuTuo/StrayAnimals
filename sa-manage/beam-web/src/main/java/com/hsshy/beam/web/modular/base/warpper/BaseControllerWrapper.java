package com.hsshy.beam.web.modular.base.warpper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.beanutils.PropertyUtilsBean;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制器查询结果的包装类基类
 *
 * @author fengshuonan
 * @date 2017年2月13日 下午10:49:36
 */
public abstract class BaseControllerWrapper {

    public Object obj = null;

    public BaseControllerWrapper(Object obj) {
        this.obj = obj;
    }

    @SuppressWarnings("unchecked")
    public Object wrap() {
        if (this.obj instanceof List) {
            List<Map> list = (List<Map>) this.obj;
            for (Map map : list) {
                wrapTheMap(map);
            }
            return list;
        }
        else if(this.obj instanceof Page){
            List<Map> mapList = new ArrayList<>();
            if((((Page) this.obj).getRecords()).size()>0){
                if(!((((Page) this.obj).getRecords()).get(0) instanceof  Map)){
                    List<Object> list = ((Page) this.obj).getRecords();
                    for(Object o:list){
                        mapList.add(beanToMap(o));
                    }
                }
                else {
                    mapList = ((Page) this.obj).getRecords();
                }

                for (Map map : mapList) {
                    wrapTheMap(map);
                }
            }

            ((Page) this.obj).setRecords(mapList);

            return this.obj;
        }
        else if (this.obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) this.obj;
            wrapTheMap(map);
            return map;
        } else {
            return this.obj;
        }
    }

    public static Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> params = new HashMap<String, Object>(0);
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!"class".equals(name)) {
                    params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    protected abstract void wrapTheMap(Map<String, Object> map);

}
