package com.shiep.service;

import com.shiep.entity.AnimalCategoryDetails;

import java.util.List;

public interface IAnimalCategoryDetailsService {
    List<AnimalCategoryDetails> queryByCategoryId(Integer id);
}
