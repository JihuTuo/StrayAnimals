package com.hsshy.beam.web.modular.blog.dao;
import com.hsshy.beam.web.modular.blog.entity.ArticleCategory;
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
 * @date 2019-08-14 10:56:42
 */
public interface ArticleCategoryMapper extends BaseMapper<ArticleCategory> {

    IPage<ArticleCategory> selectPageList(Page page, @Param("articleCategory") ArticleCategory articleCategory);

    void changeFrozen(@Param("id") Long id, @Param("frozen") Integer frozen);

    int countAcRef(@Param("categoryId") Long categoryId);

    List<ArticleCategory> getArticleCategoryList();

}
