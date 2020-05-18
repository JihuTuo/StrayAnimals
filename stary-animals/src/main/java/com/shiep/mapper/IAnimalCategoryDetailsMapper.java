package com.shiep.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiep.entity.AnimalCategoryDetails;
import com.shiep.vo.Category;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IAnimalCategoryDetailsMapper extends BaseMapper<AnimalCategoryDetails> {

    @Select("SELECT sac.type as type, sacd.name as name\n" +
            "FROM sa_animal_category sac\n" +
            "\tJOIN sa_animal_category_details sacd\n" +
            "\tON sac.id = sacd.sa_animal_category_id\n" +
            "\tWHERE sacd.id = #{categoryDetailsid}")
    Category getCategoryById(Integer categoryDetailsid);


    @Select("SELECT id FROM sa_animal_category_details WHERE sa_animal_category_id = #{categoryId}")
    List<Integer> getAnimalCategoryDetailsIdByCategoryId(Integer categoryId);


}
