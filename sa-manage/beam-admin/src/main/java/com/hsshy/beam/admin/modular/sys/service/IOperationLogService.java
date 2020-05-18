package com.hsshy.beam.admin.modular.sys.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hsshy.beam.admin.modular.sys.entity.OperationLog;

/**
 * 操作日志
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2019-04-12 14:12:44
 */
public interface IOperationLogService extends IService<OperationLog> {

    IPage<OperationLog> selectPageList(Page page, OperationLog operationLog);

    void  deleteAll();



}
