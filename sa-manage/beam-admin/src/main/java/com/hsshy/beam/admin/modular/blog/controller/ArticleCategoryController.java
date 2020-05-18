package com.hsshy.beam.admin.modular.blog.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hsshy.beam.web.modular.blog.entity.ArticleCategory;
import com.hsshy.beam.web.modular.blog.service.IArticleCategoryService;
import com.hsshy.beam.web.modular.base.controller.BaseController;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.common.utils.ToolUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Arrays;
import java.util.List;

/**
 * @author hs
 * @email 457030599@qq.com
 * @date 2019-08-14 10:56:42
 */
@Api(value = "ArticleCategoryController", tags = {"ArticleCategory接口"})
@RequestMapping("/blog/articlecategory")
@RestController
public class ArticleCategoryController extends BaseController {

    @Autowired
    private IArticleCategoryService articleCategoryService;

    //分页
    @RequiresPermissions("blog:category:list")
    @ApiOperation("分页列表")
    @GetMapping(value = "/page/list")
    public R pageList(ArticleCategory articleCategory) {
        QueryWrapper<ArticleCategory> qw = new QueryWrapper();
        if(ToolUtil.isNotEmpty(articleCategory.getName())){
            qw.like("name",articleCategory.getName());
        }
        qw.orderByDesc("sort");
        IPage page = articleCategoryService.page(new Page(articleCategory.getCurrentPage(), articleCategory.getPageSize()), qw);
        return R.ok(page);
    }
    @RequiresPermissions("blog:category:list")
    @ApiOperation("列表")
    @GetMapping(value = "/list")
    public R list(ArticleCategory articleCategory) {
        QueryWrapper<ArticleCategory> qw = new QueryWrapper();
        qw.eq("frozen",1);
        List<ArticleCategory> articleCategoryList = articleCategoryService.list(qw);
        return R.ok(articleCategoryList);
    }


    @RequiresPermissions("blog:category:save")
    @ApiOperation("保存")
    @PostMapping(value = "/save")
    public R save(@RequestBody ArticleCategory articleCategory) {
        Integer count = articleCategoryService.count(new QueryWrapper<ArticleCategory>().eq("name",articleCategory.getName()));
        if(count>0){
            return R.fail("分类已存在");
        }

        articleCategoryService.saveOrUpdate(articleCategory);
        return R.ok();
    }

    @RequiresPermissions("blog:category:del")
    @ApiOperation("删除")
    @PostMapping(value = "/delete")
    public R delete(@RequestBody Long articleCategoryIds[]) {
        if (ToolUtil.isEmpty(articleCategoryIds) || articleCategoryIds.length <= 0) {
            return R.fail("未提交要删除的记录");
        }
        Boolean flag = true;
        for(int i=0;i<articleCategoryIds.length;i++){
            int count = articleCategoryService.countAcRef(articleCategoryIds[i]);
            if(count>0){
                flag = false;
                break;
            }
        }
        if(flag){
            articleCategoryService.removeByIds(Arrays.asList(articleCategoryIds));
        }
        else {
            return R.fail("请先文章与分类的关联，再进行删除");
        }
        return R.ok();
    }

    @RequiresPermissions("blog:category:change")
    @ApiOperation("改变状态,是否可用")
    @PostMapping(value = "/change/status/{flag}")
    public R changeStatus(@RequestBody Long id, @PathVariable Integer flag) {
        articleCategoryService.changeFrozen(id, flag);
        return R.ok("修改成功");
    }


}