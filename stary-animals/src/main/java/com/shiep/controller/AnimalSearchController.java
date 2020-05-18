package com.shiep.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shiep.entity.*;
import com.shiep.mapper.IAnimalSearchCommentMapper;
import com.shiep.mapper.IAnimalSearchMapper;
import com.shiep.service.IAnimalSearchService;
import com.shiep.vo.AnimalAdoptCommentVo;
import com.shiep.vo.AnimalAdoptVo;
import com.shiep.vo.AnimalSearchCommentVo;
import com.shiep.vo.AnimalSearchVo;
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
@RequestMapping("search")
public class AnimalSearchController {
    @Resource
    private IAnimalSearchService animalSearchService;

    @Resource
    private IAnimalSearchMapper animalSearchMapper;

    @Resource
    private IAnimalSearchCommentMapper animalSearchCommentMapper;

    @PostMapping("save")
    public ResponseEntity<Long> saveAnimalSearch(AnimalSearch animalSearch) {
        if (this.animalSearchService.saveAnimalSearch(animalSearch)) {
            Long lastAutoIncrementId = this.animalSearchMapper.getLastAutoIncrementId();
            return ResponseEntity.ok(lastAutoIncrementId);
        }
        return ResponseEntity.badRequest().build();
    }

    // 获取全部adopt
    @PostMapping("queryAllAnimalSearchInPage")
    public ResponseEntity<PageInfo<AnimalSearchVo>> queryAllAnimalSearchInPage (
            int size, int page,
            Long cityId, Integer categoryId,
            Long countyId, Integer searchAnimal) {
        Page p = PageHelper.startPage(page , size);
        List<AnimalSearchVo> animalSearchVos = this.animalSearchService.queryAll(cityId, categoryId, countyId, searchAnimal);
        PageInfo<AnimalSearchVo> pageInfo = new PageInfo(animalSearchVos);
        pageInfo.setPages(p.getPages());//总页数
        pageInfo.setTotal(p.getTotal());//总条数
        return ResponseEntity.ok(pageInfo);
    }

    @GetMapping("queryAllAnimalSearchByUserId")
    public  ResponseEntity<PageInfo<AnimalSearch>> queryAnimalSearchByUserIdInPage(
            int size, int page,
            @RequestParam("userId") Long userId) {
        Page p = PageHelper.startPage(page + 1, size);
        List<AnimalSearch> searches = this.animalSearchService.queryAllByUserId(userId);
        PageInfo<AnimalSearch> pageInfo = new PageInfo(searches);
        if (CollectionUtils.isEmpty(searches)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pageInfo);
    }

    @GetMapping("deleteById")
    public ResponseEntity<Void> removeAnimalSearchById(@RequestParam("id")Long id) {
        if (this.animalSearchService.removeAnimalSearchById(id)) {
            return ResponseEntity.status(200).build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("getAnimalSearchDetailsById")
    public ResponseEntity<AnimalSearchVo> getAnimalSearchDetailsById(@RequestParam("id")Long id ) {
        AnimalSearchVo seatchVos = this.animalSearchService.getAnimalSearchDetailsById(id);
        if (seatchVos == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(seatchVos);
    }

    @PostMapping("saveComment")
    public ResponseEntity<Void> saveComment(AnimalSearchComment searchComment) {
        if (this.animalSearchService.saveSearchComment(searchComment)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("queryCommentBySearchId")
    public ResponseEntity<List<AnimalSearchCommentVo>> queryCommentBySearchId(Long searchId) {
        List<AnimalSearchCommentVo> commentVos = this.animalSearchService.queryCommentBySearchId(searchId);
        if (CollectionUtils.isEmpty(commentVos)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(commentVos);
    }

    // 后台=====================================
    @GetMapping("queryAllSearchInPageBack")
    public ResponseEntity<PageInfo<AnimalSearchVo>> queryAllSearchInPageBack(
            int size, int page) {
        Page p = PageHelper.startPage(page, size);
        List<AnimalSearchVo> searchVos = this.animalSearchService.queryAllSearchInPageBack();

        PageInfo<AnimalSearchVo> pageInfo = new PageInfo(searchVos);

        pageInfo.setPages(p.getPages());//总页数
        pageInfo.setTotal(p.getTotal());//总条数

        return ResponseEntity.ok(pageInfo);
    }


    @PostMapping("deleteMultipleSearchByIds")
    public ResponseEntity<Void> deleteMultipleSearchByIds(@RequestParam("ids") List<Long> ids) {
        if (this.animalSearchService.deleteMultipleSearchByIds(ids)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("rollbackMultipleSearchByIds")
    public ResponseEntity<Void> rollbackMultipleSearchByIds(@RequestParam("ids") List<Long> ids) {
        if (this.animalSearchService.rollbackMultipleSearchByIds(ids)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("getAnimalSearchById")
    public ResponseEntity<AnimalSearch> getAnimalSearchById(Long id) {
        AnimalSearch animalSearch = this.animalSearchService.queryAnimalSearchById(id);
        if (animalSearch == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(animalSearch);
    }

    @GetMapping("getPhotoBySearchId")
    public ResponseEntity<List<PhotoAnimalSearch>> getPhotoBySearchId(Long id) {
        List<PhotoAnimalSearch> photoAnimalSearches = this.animalSearchService.queryPhotoBySearchId(id);
        if (CollectionUtils.isEmpty(photoAnimalSearches)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(photoAnimalSearches);
    }

    @PostMapping("updateSearchBack")
    public ResponseEntity<Void> updateSearchBack(AnimalSearch editSearchInfo) {
        boolean back = this.animalSearchService.updateSearchBack(editSearchInfo);
        if (back) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("likeSearch")
    public ResponseEntity<PageInfo<AnimalSearchVo>> likeSearch(
            @RequestParam(value = "key", required = false)String key, @RequestParam(value = "value", required = false)String value,
            @RequestParam("page")Integer page, @RequestParam("size")Integer size) {
        Page p = PageHelper.startPage(page, size);

        List<AnimalSearchVo> searchVos = this.animalSearchService.querySearchByLike(key, value);

        PageInfo<AnimalSearchVo> pageInfo = new PageInfo(searchVos);

        pageInfo.setPages(p.getPages());//总页数
        pageInfo.setTotal(p.getTotal());//总条数

        return ResponseEntity.ok(pageInfo);
    }

    @GetMapping("queryAllCommentInPageBack")
    public ResponseEntity<PageInfo<AnimalSearchCommentVo>> queryAllCommentInPageBack(
            int size, int page) {
        Page p = PageHelper.startPage(page, size);
        List<AnimalSearchCommentVo> commentVos = this.animalSearchService.queryAllCommentInPageBack();

        PageInfo<AnimalSearchCommentVo> pageInfo = new PageInfo(commentVos);

        pageInfo.setPages(p.getPages());//总页数
        pageInfo.setTotal(p.getTotal());//总条数

        return ResponseEntity.ok(pageInfo);
    }

    @GetMapping("deleteCommentById")
    public ResponseEntity<Void> deleteCommentById(Long id) {
        if (this.animalSearchService.deleteCommentById(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("deleteMultipleCommentsByIds")
    public ResponseEntity<Void> deleteMultipleCommentsByIds(@RequestParam("ids") List<Long> ids) {
        if (this.animalSearchService.deleteMultipleCommentsByIds(ids)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("rollbackMultipleCommentsByIds")
    public ResponseEntity<Void> rollbackMultipleCommentsByIds(@RequestParam("ids") List<Long> ids) {
        if (this.animalSearchService.rollbackMultipleCommentsByIds(ids)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("commentLikeSearch")
    public ResponseEntity<PageInfo<AnimalSearchCommentVo>> queryAllCommentLikeInPageBack(
            int size, int page, String key, String value) {
        Page p = PageHelper.startPage(page, size);

        List<AnimalSearchCommentVo> commentVos = this.animalSearchService.queryAllCommentLikeInPageBack(key, value);

        PageInfo<AnimalSearchCommentVo> pageInfo = new PageInfo(commentVos);

        pageInfo.setPages(p.getPages());//总页数
        pageInfo.setTotal(p.getTotal());//总条数

        return ResponseEntity.ok(pageInfo);
    }
}

