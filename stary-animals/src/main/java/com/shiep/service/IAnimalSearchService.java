package com.shiep.service;


import com.shiep.entity.AnimalSearch;
import com.shiep.entity.AnimalSearchComment;
import com.shiep.entity.PhotoAnimalSearch;
import com.shiep.vo.AnimalAdoptCommentVo;
import com.shiep.vo.AnimalSearchCommentVo;
import com.shiep.vo.AnimalSearchVo;

import java.beans.Transient;
import java.util.List;

public interface IAnimalSearchService  {

    @Transient
    boolean saveAnimalSearch(AnimalSearch animalSearch);

    @Transient
    List<AnimalSearchVo> queryAll(Long cityId, Integer categoryId, Long countyId, Integer searchAnimal);

    List<AnimalSearch> queryAllByUserId(Long userId);

    @Transient
    public boolean removeAnimalSearchById(Long id);

    public AnimalSearchVo getAnimalSearchDetailsById(Long id);

    @Transient
    boolean saveSearchComment(AnimalSearchComment searchComment);

    List<AnimalSearchCommentVo> queryCommentBySearchId(Long searchId);

    List<AnimalSearchVo> queryAllSearchInPageBack();

    @Transient
    boolean deleteMultipleSearchByIds(List<Long> ids);

    @Transient
    boolean rollbackMultipleSearchByIds(List<Long> ids);

    AnimalSearch queryAnimalSearchById(Long id);

    List<PhotoAnimalSearch> queryPhotoBySearchId(Long id);

    @Transient
    boolean updateSearchBack(AnimalSearch animalSearchInfo);

    List<AnimalSearchVo> querySearchByLike(String key, String value);

    List<AnimalSearchCommentVo> queryAllCommentInPageBack();

    @Transient
    boolean deleteCommentById(Long id);

    @Transient
    boolean deleteMultipleCommentsByIds(List<Long> ids);

    @Transient
    boolean rollbackMultipleCommentsByIds(List<Long> ids);

    List<AnimalSearchCommentVo> queryAllCommentLikeInPageBack(String key, String value);
}
