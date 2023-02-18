package com.shiep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiep.entity.PhotoAnimalAdopt;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IPhotoAnimalAdoptMapper extends BaseMapper<PhotoAnimalAdopt> {

    @Select("SELECT id, name, photo, sa_animal_adopt_id\n" +
            "FROM sa_photo_adopt\n" +
            "WHERE sa_animal_adopt_id = #{animalAdoptId}")
    List<PhotoAnimalAdopt> getPhotoByAnimalAdoptId(Long animalAdoptId);

    @Select("SELECT * FROM sa_photo_adopt")
    List<PhotoAnimalAdopt> findAll();
}
