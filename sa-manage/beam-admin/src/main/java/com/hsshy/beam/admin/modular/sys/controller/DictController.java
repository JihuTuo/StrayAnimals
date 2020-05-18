package com.hsshy.beam.admin.modular.sys.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hsshy.beam.admin.modular.sys.entity.Dict;
import com.hsshy.beam.admin.modular.sys.service.IDictService;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.common.utils.ToolUtil;
import com.hsshy.beam.web.modular.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Arrays;
import java.util.List;

/**
 * 字典表
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2019-04-12 16:45:03
 */
@Api(value="DictController",tags={"Dict接口"})
@RequestMapping("/sys/dict")
@RestController
public class DictController extends BaseController {

    @Autowired
    private IDictService dictService;


    //分页
    @ApiOperation("分页列表")
    @GetMapping(value = "/page/list")
    @RequiresPermissions("sys:dict:list")
    public R pageList(Dict dict){

        QueryWrapper<Dict> qw = new QueryWrapper();
        if(ToolUtil.isNotEmpty(dict.getPid())){
            qw.eq("pid",dict.getPid());
        }
        else {
            qw.eq("pid",0);
        }
        if(ToolUtil.isNotEmpty(dict.getKeyword())){
            qw.and(wrapper -> wrapper.like("name", dict.getKeyword()).or().like("code", dict.getKeyword()));
        }

        IPage page = dictService.page(new Page(dict.getCurrentPage(),dict.getPageSize()),qw);
        return R.ok(page);
    }
    @ApiOperation("列表")
    @GetMapping(value = "/list")
    public R list(Dict dict){

        QueryWrapper qw = new QueryWrapper<Dict>();
        qw.eq("pid",0);
        List<Dict> dictList = dictService.list(qw);
        return R.ok(dictList);
    }
    @ApiOperation("保存")
    @RequiresPermissions(value = {"sys:dict:add","sys:dict:edit"},logical = Logical.OR)
    @PostMapping(value = "/save")
    public R save(@RequestBody Dict dict){

        Assert.notNull(dict.getCode(),"编码不能为空");
        Assert.notNull(dict.getName(),"名称不能为空");
        Assert.notNull(dict.getPid(),"父级字典不能为空");
        Dict old = dictService.getOne(new QueryWrapper<Dict>().eq("code",dict.getCode()));
        if(ToolUtil.isNotEmpty(old)){
            if(!old.getId().equals(dict.getId())){
                return R.fail("编码重复，请换一个编码值");
            }
        }

        dictService.saveOrUpdate(dict);
        return R.ok();
    }
    @ApiOperation("删除")
    @RequiresPermissions("sys:dict:del")
    @PostMapping(value = "/delete")
    public R delete(@RequestBody Long dictIds[]){

        if(ToolUtil.isEmpty(dictIds)||dictIds.length<=0){
            return R.fail("未提交要删除的记录");
        }
        List<Dict> dictList = (List<Dict>) dictService.listByIds(Arrays.asList(dictIds));
        for(Dict dict:dictList){
            Integer count = dictService.count(new QueryWrapper<Dict>().eq("pid",dict.getId()));
            if(count>0){
                return R.fail("该字典存在子字典，请先删除其子字典！");
            }
        }
        dictService.removeByIds(Arrays.asList(dictIds));
        return R.ok();
    }






}