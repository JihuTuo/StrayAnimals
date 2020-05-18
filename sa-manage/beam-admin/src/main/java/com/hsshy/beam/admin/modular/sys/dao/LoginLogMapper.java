package com.hsshy.beam.admin.modular.sys.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hsshy.beam.admin.modular.sys.entity.LoginLog;
import org.apache.ibatis.annotations.Param;
/**
 * 登陆日志
 * 
 * @author hs
 * @email 457030599@qq.com
 * @date 2019-04-12 14:08:56
 */
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    void deleteAll();

    IPage<LoginLog> selectPageList(Page page, @Param("loginLog") LoginLog loginLog);

}
