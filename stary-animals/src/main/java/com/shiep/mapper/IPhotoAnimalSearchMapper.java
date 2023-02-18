package com.shiep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiep.entity.PhotoAnimalAdopt;
import com.shiep.entity.PhotoAnimalSearch;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IPhotoAnimalSearchMapper extends BaseMapper<PhotoAnimalSearch> {

    @Select("SELECT id, name, photo, sa_animal_search_id\n" +
            "FROM sa_photo_search\n" +
            "WHERE sa_animal_search_id = #{animalSearchId}")
    List<PhotoAnimalSearch> getPhotoByAnimalSearchId(Long animalSearchId);

    @Select("SELECT * FROM sa_photo_search")
    List<PhotoAnimalSearch> findAll();
}
