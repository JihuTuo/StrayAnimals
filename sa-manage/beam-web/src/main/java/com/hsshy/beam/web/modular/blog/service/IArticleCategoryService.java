package com.hsshy.beam.web.modular.blog.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hsshy.beam.web.modular.blog.entity.ArticleCategory;

import java.util.List;

/**
 * 
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2019-08-14 10:56:42
 */
public interface IArticleCategoryService extends IService<ArticleCategory> {

    void changeFrozen(Long id, Integer frozen);

    int countAcRef(Long categoryId);

    List<ArticleCategory> getArticleCategoryList();

}
