package com.shiep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiep.entity.AnimalAdopt;
import com.shiep.entity.AnimalSearch;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface IAnimalSearchMapper extends BaseMapper<AnimalSearch> {

    @Select("select max(id) from sa_animal_search;")
    Long getLastAutoIncrementId();


    @Update("UPDATE sa_animal_search SET delete_status = 1 WHERE id = #{id}")
    Integer removeAnimalSearchById(Long id);

    @Update("UPDATE sa_animal_search SET delete_status = 0 WHERE id = #{id}")
    int rollbackMultipleSearchById(Long id);
}
