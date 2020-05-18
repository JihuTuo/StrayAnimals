package com.hsshy.beam.admin.modular.sys.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hsshy.beam.admin.common.annotion.SysLog;
import com.hsshy.beam.admin.modular.sys.entity.OperationLog;
import com.hsshy.beam.admin.modular.sys.service.IOperationLogService;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.web.modular.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 操作日志
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2019-04-12 15:16:45
 */
@Api(value="OperationLogController",tags={"OperationLog接口"})
@RequestMapping("/sys/operationlog")
@RestController
public class OperationLogController extends BaseController {

    @Autowired
    private IOperationLogService operationLogService;


    //分页
    @ApiOperation("分页列表")
    @GetMapping(value = "/page/list")
    @RequiresPermissions("sys:operationLog:list")
    public R pageList(OperationLog operationLog){


        IPage page = operationLogService.selectPageList(new Page(operationLog.getCurrentPage(),operationLog.getPageSize()),operationLog);
        return R.ok(page);
    }
    @ApiOperation("列表")
    @GetMapping(value = "/list")
    public R list(OperationLog operationLog){
        QueryWrapper qw = new QueryWrapper<OperationLog>();
        List<OperationLog> operationLogList = operationLogService.list(qw);
        return R.ok(operationLogList);
    }

    @SysLog(value = "清空日志")
    @ApiOperation("清空")
    @PostMapping(value = "/clear")
    @RequiresPermissions("sys:operationLog:clear")
    public R clear() {
        operationLogService.deleteAll();
        return R.ok();
    }






}