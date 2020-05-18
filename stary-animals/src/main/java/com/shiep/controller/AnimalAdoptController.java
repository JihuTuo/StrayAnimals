package com.shiep.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shiep.entity.*;
import com.shiep.mapper.IAnimalAdoptMapper;
import com.shiep.mapper.IPhotoAnimalAdoptMapper;
import com.shiep.service.IAnimalAdoptService;
import com.shiep.vo.AnimalAdoptCommentVo;
import com.shiep.vo.AnimalAdoptVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("adopt")
public class AnimalAdoptController {
    @Autowired  //  记住，我们一般注入的是接口 而不是实现类
    private IAnimalAdoptService animalAdoptService;

    @Resource
    private IAnimalAdoptMapper animalAdoptMapper;



    @PostMapping("save")
    public ResponseEntity<Long> saveAnimalAdopt(AnimalAdopt animalAdopt) {
        boolean flag = this.animalAdoptService.saveAnimalAdopt(animalAdopt);
        if (flag) {
            Long lastId = this.animalAdoptMapper.getLastAutoIncrementId();
            return ResponseEntity.ok(lastId);
        }
        return ResponseEntity.badRequest().build();
    }

    // 获取全部adopt
   /* @GetMapping("queryAllAnimalAdopt")
    public ResponseEntity<List<AnimalAdoptVo>> queryAllAnimalAdopt() {
        List<AnimalAdoptVo> adoptVos = this.animalAdoptService.queryAll();
        if (CollectionUtils.isEmpty(adoptVos)) {
            return ResponseEntity.status(201).build();
        }
        return ResponseEntity.ok(adoptVos);
    }*/


    @GetMapping("getTotal")
    public ResponseEntity<Integer> getAllAnimalAdoptCount() {
        Integer count = this.animalAdoptMapper.selectCount(null);
        if (count == null) {
            return null;
        }
        return ResponseEntity.ok(count);
    }


    // 获取全部adopt
    @PostMapping("queryAllAnimalAdoptInPage")
    public ResponseEntity<PageInfo<AnimalAdoptVo>> queryAllAnimalAdoptInPage(
            int size, int page,
            Long cityId, Integer categoryId,
            Long countyId) {
        Page p = PageHelper.startPage(page, size);
        List<AnimalAdoptVo> adoptVos = this.animalAdoptService.queryAll(cityId, categoryId, countyId);

        //PageImpl page = new PageImpl(customerDTOS, PageRequest.of(0, 20), customerDTOS.size());

        //System.out.println("--" + adoptVos) ;
        PageInfo<AnimalAdoptVo> pageInfo = new PageInfo(adoptVos);

        pageInfo.setPages(p.getPages());//总页数
        pageInfo.setTotal(p.getTotal());//总条数

        return ResponseEntity.ok(pageInfo);
    }

    @GetMapping("queryAllAnimalAdoptByUserId")
    public  ResponseEntity<PageInfo<AnimalAdopt>> queryAnimalAdoptByUserIdInPage(
            int size, int page,
            @RequestParam("userId") Long userId) {
        Page p = PageHelper.startPage(page + 1, size);
        List<AnimalAdopt> adopts = this.animalAdoptService.queryAdoptByUserId(userId);
        PageInfo<AnimalAdopt> pageInfo = new PageInfo(adopts);
        if (CollectionUtils.isEmpty(adopts)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pageInfo);
    }

    @GetMapping("deleteById")
    public ResponseEntity<Void> removeAnimalAdoptById(@RequestParam("id")Long id) {
        if (this.animalAdoptService.removeAnimalAdoptById(id)) {
            return ResponseEntity.status(200).build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("getAnimalAdoptDetailsById")
    public ResponseEntity<AnimalAdoptVo> getAnimalAdoptDetailsById(@RequestParam("id")Long id ) {
        AnimalAdoptVo adoptVo = this.animalAdoptService.getAnimalAdoptDetailsById(id);
        if (adoptVo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adoptVo);
    }

    @PostMapping("saveComment")
    public ResponseEntity<Void> saveComment(AnimalAdoptComment adoptComment) {
        if (this.animalAdoptService.saveAdoptComment(adoptComment)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("queryCommentByAdoptId")
    public ResponseEntity<List<AnimalAdoptCommentVo>> queryCommentByAdoptId(Long adoptId) {
        List<AnimalAdoptCommentVo> commentVos = this.animalAdoptService.queryCommentByAdoptId(adoptId);
        if (CollectionUtils.isEmpty(commentVos)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(commentVos);
    }



    // 后台方法

    @GetMapping("queryAllAdoptInPageBack")
    public ResponseEntity<PageInfo<AnimalAdoptVo>> queryAllAdoptInPageBack(
            int size, int page) {
        Page p = PageHelper.startPage(page, size);
        List<AnimalAdoptVo> adoptVos = this.animalAdoptService.queryAllAdoptInPageBack();

        PageInfo<AnimalAdoptVo> pageInfo = new PageInfo(adoptVos);

        pageInfo.setPages(p.getPages());//总页数
        pageInfo.setTotal(p.getTotal());//总条数

        return ResponseEntity.ok(pageInfo);
    }


    @GetMapping("queryAllAnimalStatus")
    public ResponseEntity<List<AnimalStatus>> queryAllAnimalStatus() {
        List<AnimalStatus> statusList = this.animalAdoptService.queryAllAnimalStatus();
        if (CollectionUtils.isEmpty(statusList)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(statusList);
    }

    @GetMapping("getAnimalAdoptById")
    public ResponseEntity<AnimalAdopt> getAnimalAdoptById(Long id) {
        AnimalAdopt animalAdopt = this.animalAdoptService.queryAnimalAdoptById(id);
        if (animalAdopt == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(animalAdopt);
    }

    @GetMapping("getPhotoByAdoptId")
    public ResponseEntity<List<PhotoAnimalAdopt>> getPhotoByAdoptId(Long id) {
        List<PhotoAnimalAdopt> photoAnimalAdopts = this.animalAdoptService.queryPhotoByAdoptId(id);
        if (CollectionUtils.isEmpty(photoAnimalAdopts)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(photoAnimalAdopts);
    }

    @PostMapping("updateAdoptBack")
    public ResponseEntity<Void> updateAdoptBack(AnimalAdopt editAdoptInfo) {
        boolean back = this.animalAdoptService.updateAdoptBack(editAdoptInfo);
        if (back) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("deleteMultipleAdoptsByIds")
    public ResponseEntity<Void> deleteMultipleAdoptsByIds(@RequestParam("ids") List<Long> ids) {
        if (this.animalAdoptService.deleteMultipleAdoptByIds(ids)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("rollbackMultipleSearchByIds")
    public ResponseEntity<Void> rollbackMultipleSearchByIds(@RequestParam("ids") List<Long> ids) {
        if (this.animalAdoptService.rollbackMultipleAdoptByIds(ids)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("likeSearch")
    public ResponseEntity<PageInfo<AnimalAdoptVo>> likeSearch(
            @RequestParam(value = "key", required = false)String key, @RequestParam(value = "value", required = false)String value,
            @RequestParam("page")Integer page, @RequestParam("size")Integer size) {
        Page p = PageHelper.startPage(page, size);

        List<AnimalAdoptVo> adoptVos = this.animalAdoptService.queryAdoptByLike(key, value);

        PageInfo<AnimalAdoptVo> pageInfo = new PageInfo(adoptVos);

        pageInfo.setPages(p.getPages());//总页数
        pageInfo.setTotal(p.getTotal());//总条数

        return ResponseEntity.ok(pageInfo);
    }


    @GetMapping("queryAllCommentInPageBack")
    public ResponseEntity<PageInfo<AnimalAdoptCommentVo>> queryAllCommentInPageBack(
            int size, int page) {
        Page p = PageHelper.startPage(page, size);
        List<AnimalAdoptCommentVo> commentVos = this.animalAdoptService.queryAllCommentInPageBack();

        PageInfo<AnimalAdoptCommentVo> pageInfo = new PageInfo(commentVos);

        pageInfo.setPages(p.getPages());//总页数
        pageInfo.setTotal(p.getTotal());//总条数

        return ResponseEntity.ok(pageInfo);
    }

    @GetMapping("deleteCommentById")
    public ResponseEntity<Void> deleteCommentById(Long id) {
        if (this.animalAdoptService.deleteCommentById(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("deleteMultipleCommentsByIds")
    public ResponseEntity<Void> deleteMultipleCommentsByIds(@RequestParam("ids") List<Long> ids) {
        if (this.animalAdoptService.deleteMultipleCommentsByIds(ids)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("rollbackMultipleCommentsByIds")
    public ResponseEntity<Void> rollbackMultipleCommentsByIds(@RequestParam("ids") List<Long> ids) {
        if (this.animalAdoptService.rollbackMultipleCommentsByIds(ids)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("commentLikeSearch")
    public ResponseEntity<PageInfo<AnimalAdoptCommentVo>> queryAllCommentLikeInPageBack(
            int size, int page, String key, String value) {
        Page p = PageHelper.startPage(page, size);

        List<AnimalAdoptCommentVo> commentVos = this.animalAdoptService.queryAllCommentLikeInPageBack(key, value);

        PageInfo<AnimalAdoptCommentVo> pageInfo = new PageInfo(commentVos);

        pageInfo.setPages(p.getPages());//总页数
        pageInfo.setTotal(p.getTotal());//总条数

        return ResponseEntity.ok(pageInfo);
    }
}
