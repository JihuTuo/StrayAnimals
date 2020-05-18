package com.hsshy.beam.web.modular.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.web.modular.blog.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2019-06-04 15:34:18
 */
public interface IArticleService extends IService<Article> {

    IPage<Article> getPage(Page page, Article article);

    Integer addReadNum(Long id);

    Article getArticle(Long id);

    Article getArticleInfo(Long id);

    R saveArticle(Article article);

    List<Article> getArticleListByCid(Long cid);

    IPage<Article> selectPageByCid(Page page,Article article);

    int delRefById(@Param("articleId") Long articleId);

}
