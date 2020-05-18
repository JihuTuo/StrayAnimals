package com.hsshy.beam.admin.modular.sys.controller;
import com.hsshy.beam.common.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 总览信息
 *
 * @author fengshuonan
 * @Date 2017年3月4日23:05:54
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    /**
     * 跳转到黑板
     */
    @RequiresPermissions("sys:dashboard:info")
    @GetMapping
    public Object blackboard() {

        return R.ok("首页");
    }
}
