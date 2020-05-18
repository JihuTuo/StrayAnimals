package com.hsshy.beam.web.modular.blog.service.impl;
import com.hsshy.beam.web.modular.blog.dao.ArticleCategoryMapper;
import com.hsshy.beam.web.modular.blog.entity.ArticleCategory;
import com.hsshy.beam.web.modular.blog.service.IArticleCategoryService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2019-08-14 10:56:42
 */
@Service
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory> implements IArticleCategoryService {


    @Override
    public void changeFrozen(Long id, Integer frozen) {
        baseMapper.changeFrozen(id,frozen);
    }

    @Override
    public int countAcRef(Long categoryId) {
        return baseMapper.countAcRef(categoryId);
    }

    @Override
    public List<ArticleCategory> getArticleCategoryList() {
        return baseMapper.getArticleCategoryList();
    }


}
