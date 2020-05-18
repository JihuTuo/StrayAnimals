package com.hsshy.beam.web.modular.blog.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.common.utils.ShortCodeKit;
import com.hsshy.beam.common.utils.ToolUtil;
import com.hsshy.beam.web.modular.blog.dao.ArticleMapper;
import com.hsshy.beam.web.modular.blog.entity.Article;
import com.hsshy.beam.web.modular.blog.service.IArticleService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2019-06-04 15:34:18
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {


    @Override
    public IPage<Article> getPage(Page page, Article article) {
        return baseMapper.getPage(page,article);
    }

    @Override
    public Integer addReadNum(Long id) {
        return baseMapper.addReadNum(id);
    }

    @Override
    public Article getArticle(Long id) {
        return baseMapper.getArticle(id);
    }

    @Override
    public Article getArticleInfo(Long id) {
        return baseMapper.getArticleInfo(id);
    }

    @Transactional
    @Override
    public R saveArticle(Article article) {
        if(ToolUtil.isNotEmpty(article.getId())){
            if(ToolUtil.isEmpty(article.getShortCode())){
                Long pid = ShortCodeKit.permutedId(article.getId());
                String shortCode = ShortCodeKit.convertDecimalToBase62(pid,8);
                article.setShortCode(shortCode);
            }
            this.updateById(article);
            baseMapper.delRefById(article.getId());
        }
        else {
            this.saveOrUpdate(article);
            Long pid = ShortCodeKit.permutedId(article.getId());
            String shortCode = ShortCodeKit.convertDecimalToBase62(pid,8);
            article.setShortCode(shortCode);
            this.updateById(article);
        }
        if(article.getCids().size()>0){
            baseMapper.saveRef(article.getId(),article.getCids());
        }
        return R.ok(article);

    }

    @Override
    public List<Article> getArticleListByCid(Long cid) {
        return baseMapper.getArticleListByCid(cid);
    }

    @Override
    public IPage<Article> selectPageByCid(Page page, Article article) {
        return baseMapper.selectPageByCid(page,article);
    }

    @Override
    public int delRefById(Long articleId) {
        return baseMapper.delRefById(articleId);
    }
}
