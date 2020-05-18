package com.hsshy.beam.admin.modular.blog.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hsshy.beam.web.modular.blog.entity.Article;
import com.hsshy.beam.web.modular.blog.service.IArticleService;
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
 * @date 2019-06-04 15:34:18
 */
@Api(value = "ArticleController", tags = {"Article接口"})
@RequestMapping("/blog/article")
@RestController
public class ArticleController extends BaseController {

    @Autowired
    private IArticleService articleService;


    //分页
    @RequiresPermissions("blog:article:list")
    @ApiOperation("分页列表")
    @GetMapping(value = "/page/list")
    public R pageList(Article article) {
        IPage page ;
        if(ToolUtil.isNotEmpty(article.getCid())){
            page = articleService.selectPageByCid(new Page(article.getCurrentPage(), article.getPageSize()), article);
        }
        else {
            page = articleService.getPage(new Page(article.getCurrentPage(), article.getPageSize()), article);

        }
        return R.ok(page);
    }
    @RequiresPermissions("blog:article:list")
    @ApiOperation("列表")
    @GetMapping(value = "/list")
    public R list(Article article) {
        QueryWrapper qw = new QueryWrapper<Article>();
        qw.eq("frozen",1);
        List<Article> articleList = articleService.list(qw);
        return R.ok(articleList);
    }
    @RequiresPermissions("blog:article:info")
    @ApiOperation("详情")
    @GetMapping(value = "/info")
    public R info(Long articleId) {
        Article article = articleService.getArticleInfo(articleId);

        return R.ok(article);
    }
    @RequiresPermissions("blog:article:save")
    @ApiOperation("保存")
    @PostMapping(value = "/save")
    public R save(@RequestBody Article article) {
        return articleService.saveArticle(article);
    }

    @RequiresPermissions("blog:article:save")
    @ApiOperation("保存内容")
    @PostMapping(value = "/save/content")
    public R saveContent(@RequestBody Article article) {
        articleService.saveOrUpdate(article);
        return R.ok("保存文章内容成功");
    }
    @RequiresPermissions("blog:article:del")
    @ApiOperation("删除")
    @PostMapping(value = "/delete")
    public R delete(@RequestBody Long articleIds[]) {

        if (ToolUtil.isEmpty(articleIds) || articleIds.length <= 0) {
            return R.fail("未提交要删除的记录");
        }
        boolean a = articleService.removeByIds(Arrays.asList(articleIds));
        if(a){
            for(int i=0;i<articleIds.length;i++){
                articleService.delRefById(articleIds[i]);
            }
        }
        return R.ok();
    }


}