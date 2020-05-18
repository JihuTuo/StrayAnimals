package com.hsshy.beam.web.modular.blog.dao;
import com.hsshy.beam.web.modular.blog.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author hs
 * @email 457030599@qq.com
 * @date 2019-06-04 15:34:18
 */
public interface ArticleMapper extends BaseMapper<Article> {

    IPage<Article> getPage(Page page, @Param("article") Article article);

    Integer addReadNum(@Param("id") Long id);

    Article getArticle(@Param("id") Long id);

    Article getArticleInfo(@Param("id") Long id);

    int delRefById(@Param("articleId") Long articleId);

    int saveRef(@Param("id") Long id,@Param("categoryIds") List<Long> categoryIds);

    List<Long> getCidById(@Param("id") Long id);

    List<Article> getArticleListByCid(@Param("cid") Long cid);

    IPage<Article> selectPageByCid(Page page,@Param("article") Article article);


}
