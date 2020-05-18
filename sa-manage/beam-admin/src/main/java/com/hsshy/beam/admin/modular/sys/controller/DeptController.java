package com.hsshy.beam.admin.modular.sys.controller;
import com.hsshy.beam.admin.modular.sys.entity.Dept;
import com.hsshy.beam.admin.modular.sys.service.IDeptService;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.common.utils.ToolUtil;
import com.hsshy.beam.web.modular.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;


/**
 * 部门管理
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2018-12-17 15:21:00
 */
@Api(value="DeptController",tags={"Dept接口"})
@RequestMapping("/sys/dept")
@RestController
public class DeptController extends BaseController {

    @Autowired
    private IDeptService deptService;



    @ApiOperation("保存")
    @PostMapping(value = "/save")
    @RequiresPermissions(value = {"sys:dept:add","sys:dept:edit"},logical = Logical.OR)
    public R save(@RequestBody Dept dept){

        if(ToolUtil.isEmpty(dept.getParentId())){
            dept.setParentId(0L);
        }
        deptService.saveOrUpdate(dept);
        return R.ok();
    }
    @ApiOperation("删除")
    @PostMapping(value = "/delete")
    @RequiresPermissions("sys:dept:del")
    public R delete(@RequestBody Long deptIds[]){

        if(ToolUtil.isEmpty(deptIds)||deptIds.length<=0){
            return R.fail("未提交要删除的记录");
        }
            deptService.removeByIds(Arrays.asList(deptIds));
        return R.ok();
    }

    /**
     * 树形
     */
    @ApiOperation(value = "树形部门")
    @RequiresPermissions("sys:dept:list")
    @GetMapping("/tree/dept")
    public R treeDept(Dept dept){
        return R.ok(deptService.treeDeptList(0L,dept));
    }


    @ApiOperation("详情")
    @GetMapping(value = "/info")
    @RequiresPermissions("sys:dept:edit")
    public R info(@RequestParam Long deptId){

        Dept dept = deptService.getById(deptId);
        if(ToolUtil.isEmpty(dept)){
            return R.fail("找不到该部门");
        }

        if(dept.getParentId()!=0){
            Dept pdept = deptService.getById(dept.getParentId());
            dept.setPname(pdept.getName());
        }
        else {
            dept.setPname("顶级");

        }
        return R.ok(dept);
    }






}