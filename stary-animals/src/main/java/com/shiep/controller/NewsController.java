package com.shiep.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shiep.entity.News;
import com.shiep.entity.NewsType;
import com.shiep.service.INewsService;
import com.shiep.vo.AnimalAdoptCommentVo;
import com.shiep.vo.NewsVo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("news")
public class NewsController {
    @Resource
    private INewsService newsService;

    @GetMapping("getAllNewsType")
    private ResponseEntity<List<NewsType>> queryAllNewsType(){
        List<NewsType> newsTypes = this.newsService.queryAllNewsType();
        if (CollectionUtils.isEmpty(newsTypes)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(newsTypes);
    }

    @GetMapping("getAllNewsVosInPage")
    public ResponseEntity<PageInfo<NewsVo>> queryAllCommentInPageBack(
            Integer size, Integer page, Integer typeId) {
        Page p = PageHelper.startPage(page, size);
        List<NewsVo> newsVos = this.newsService.queryAllNewsVos(typeId);

        PageInfo<NewsVo> pageInfo = new PageInfo(newsVos);

        pageInfo.setPages(p.getPages());//总页数
        pageInfo.setTotal(p.getTotal());//总条数

        return ResponseEntity.ok(pageInfo);
    }


    @GetMapping("getAllNewsVosInPageBack")
    public ResponseEntity<PageInfo<NewsVo>> getAllNewsVosInPageBack(
            Integer size, Integer page) {
        Page p = PageHelper.startPage(page, size);
        List<NewsVo> newsVos = this.newsService.queryAllNewsVos();

        PageInfo<NewsVo> pageInfo = new PageInfo(newsVos);

        pageInfo.setPages(p.getPages());//总页数
        pageInfo.setTotal(p.getTotal());//总条数

        return ResponseEntity.ok(pageInfo);
    }


    @PostMapping("saveNews")
    public ResponseEntity<Void> saveNews(News news) {
        if (this.newsService.saveNews(news)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("deleteNewsById")
    public ResponseEntity<Void> deleteNewsById(Long id) {
        if (this.newsService.deleteNewsById(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("deleteMultipleNewsByIds")
    public ResponseEntity<Void> deleteMultipleNewsByIds(@RequestParam("ids")List<Long> ids) {
        if (this.newsService.deleteNewsByIds(ids)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("rollbackMultipleNewsByIds")
    public ResponseEntity<Void> rollbackMultipleNewsByIds(@RequestParam("ids")List<Long> ids) {
        if (this.newsService.rollbackNewsById(ids)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("getNewsById")
    public ResponseEntity<News> getNewsVoById(Long id) {
        News news = this.newsService.getNewsById(id);
        if (news == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(news);
    }

    @PostMapping("updateNewsBack")
    public ResponseEntity<Void> updateNewsBack(News editNews) {
        if (this.newsService.updateNews(editNews)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("getNewsDetailsById")
    public ResponseEntity<NewsVo> getNewsDetailsById(Long id) {
        NewsVo newsVo = this.newsService.getNewsDetailsById(id);
        if (newsVo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(newsVo);
    }

}
