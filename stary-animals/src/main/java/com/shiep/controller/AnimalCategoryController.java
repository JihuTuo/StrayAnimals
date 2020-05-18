package com.shiep.controller;

import com.shiep.entity.AnimalCategory;
import com.shiep.entity.AnimalCategoryDetails;
import com.shiep.service.IAnimalAdoptService;
import com.shiep.service.impl.AnimalCategoryDetailsServiceImpl;
import com.shiep.service.impl.AnimalCategoryServiceImpl;
import com.shiep.vo.AnimalAdoptVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "animalCategory")
public class AnimalCategoryController {
    @Autowired
    private AnimalCategoryServiceImpl animalCategoryService;

    @Resource
    private AnimalCategoryDetailsServiceImpl ACDService;

    @Resource
    private IAnimalAdoptService animalAdoptService;

    /*获得全部分类*/
    @GetMapping(value = "queryAll")
    public ResponseEntity<List<AnimalCategory>> queryAll() {
        List<AnimalCategory> animalCategories = this.animalCategoryService.queryAll();
        if (CollectionUtils.isEmpty(animalCategories)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(animalCategories);
    }

    /*获得下一级具体的分类*/
    @GetMapping(value = "queryByCategoryId")
    public ResponseEntity<List<AnimalCategoryDetails>> queryAnimalCategoryDetailsByCategoryId(Integer id) {
        List<AnimalCategoryDetails> anilmalCategoryDetails = this.ACDService.queryByCategoryId(id);
        if (CollectionUtils.isEmpty(anilmalCategoryDetails)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(anilmalCategoryDetails);
    }


}
