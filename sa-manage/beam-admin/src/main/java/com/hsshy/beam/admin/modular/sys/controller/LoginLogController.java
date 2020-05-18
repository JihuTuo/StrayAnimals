package com.hsshy.beam.admin.modular.sys.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hsshy.beam.admin.common.annotion.SysLog;
import com.hsshy.beam.admin.modular.sys.entity.LoginLog;
import com.hsshy.beam.admin.modular.sys.service.ILoginLogService;
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
 * 登陆日志
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2019-04-12 14:08:56
 */
@Api(value = "LoginLogController", tags = {"LoginLog接口"})
@RequestMapping("/sys/loginlog")
@RestController
public class LoginLogController extends BaseController {

    @Autowired
    private ILoginLogService loginLogService;


    //分页
    @ApiOperation("分页列表")
    @GetMapping(value = "/page/list")
    @RequiresPermissions("sys:loginLog:list")
    public R pageList(LoginLog loginLog) {


        IPage page = loginLogService.selectPageList(new Page(loginLog.getCurrentPage(), loginLog.getPageSize()), loginLog);
        return R.ok(page);
    }

    @ApiOperation("列表")
    @GetMapping(value = "/list")
    public R list(LoginLog loginLog) {

        QueryWrapper qw = new QueryWrapper<LoginLog>();

        List<LoginLog> loginLogList = loginLogService.list(qw);
        return R.ok(loginLogList);
    }

    @SysLog(value = "清空日志")
    @ApiOperation("清空")
    @PostMapping(value = "/clear")
    @RequiresPermissions("sys:loginLog:clear")
    public R clear() {
        loginLogService.deleteAll();
        return R.ok();
    }


}