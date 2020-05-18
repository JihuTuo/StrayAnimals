package com.hsshy.beam.admin.modular.sys.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hsshy.beam.admin.modular.sys.entity.OperationLog;
import org.apache.ibatis.annotations.Param;
/**
 * 操作日志
 * 
 * @author hs
 * @email 457030599@qq.com
 * @date 2019-04-12 14:12:44
 */
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    IPage<OperationLog> selectPageList(Page page, @Param("operationLog") OperationLog operationLog);

    void deleteAll();


}
