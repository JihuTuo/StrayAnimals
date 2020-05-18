package com.shiep.service.impl;

import com.shiep.mapper.IAnimalCategoryMapper;
import com.shiep.entity.AnimalCategory;
import com.shiep.service.IAnimalCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AnimalCategoryServiceImpl implements IAnimalCategoryService {
    @Resource
    private IAnimalCategoryMapper animalCategoryMapper;


    @Override
    public List<AnimalCategory> queryAll() {
        // 即查询全部
        return this.animalCategoryMapper.selectList(null);
    }
}
