package com.shiep.service;

import com.shiep.entity.*;
import com.shiep.vo.AnimalAdoptCommentVo;
import com.shiep.vo.AnimalAdoptVo;
import org.apache.ibatis.annotations.Update;

import java.beans.Transient;
import java.util.List;

public interface IAnimalAdoptService {
    boolean saveAnimalAdopt(AnimalAdopt animalAdopt);

    List<AnimalAdoptVo> queryAll(Long cityId, Integer categoryId, Long countyId);

    List<AnimalAdoptVo> queryAllAdoptInPageBack();

    List<AnimalAdopt> queryAdoptByUserId(Long userId);

    @Transient
    boolean removeAnimalAdoptById(Long id);

    AnimalAdoptVo getAnimalAdoptDetailsById(Long id);

    List<AnimalStatus> queryAllAnimalStatus();

    AnimalAdopt queryAnimalAdoptById(Long id);

    List<PhotoAnimalAdopt> queryPhotoByAdoptId(Long id);

    @Transient
    boolean updateAdoptBack(AnimalAdopt editAdoptInfo);

    @Transient
    boolean deleteMultipleAdoptByIds(List<Long> ids);

    List<AnimalAdoptVo> queryAdoptByLike(String key, String value);

    @Transient
    boolean saveAdoptComment(AnimalAdoptComment adoptComment);

    List<AnimalAdoptCommentVo> queryCommentByAdoptId(Long adoptId);

    List<AnimalAdoptCommentVo> queryAllCommentInPageBack();

    @Transient
    boolean deleteCommentById(Long id);

    @Transient
    boolean deleteMultipleCommentsByIds(List<Long> ids);

    @Transient
    boolean rollbackMultipleCommentsByIds(List<Long> ids);

    List<AnimalAdoptCommentVo> queryAllCommentLikeInPageBack(String key, String value);

    boolean rollbackMultipleAdoptByIds(List<Long> ids);
}
