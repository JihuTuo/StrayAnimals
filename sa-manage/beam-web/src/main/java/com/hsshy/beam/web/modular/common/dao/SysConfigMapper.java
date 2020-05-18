package com.hsshy.beam.web.modular.common.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hsshy.beam.web.modular.common.entity.SysConfig;
import org.apache.ibatis.annotations.Param;

/**
 * 角色
 * 
 * @author hs
 * @email 457030599@qq.com
 * @date 2018-10-10 21:13:03
 */
public interface SysConfigMapper extends BaseMapper<SysConfig> {

    /**
     * 根据key，查询value
     */
    String getByKey(@Param("key") String key);

    /**
     * 根据key，查询value
     */
    SysConfig getConfigByKey(@Param("key") String key);

    /**
     * 根据key，更新value
     */
    int updateValueByKey(@Param("key") String key, @Param("value") String value);
	
}
