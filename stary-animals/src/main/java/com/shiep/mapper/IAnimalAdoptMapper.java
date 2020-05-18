package com.shiep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiep.entity.AnimalAdopt;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.beans.Transient;
import java.util.List;

public interface IAnimalAdoptMapper extends BaseMapper<AnimalAdopt> {

    @Select("select max(id) from sa_animal_adopt;")
    Long getLastAutoIncrementId();


    @Select("SELECT saa.id, saa.title, saa.sex, saa.amount, saa.age, saa.message, saa.person_name, saa.phone, saa.we_chat, saa.create_time,\n" +
            "\t\t saa.delete_status, category.type as type, category.name as name\n" +
            "FROM (SELECT * FROM sa_animal_adopt WHERE delete_status = 0) saa\n" +
            "\tjoin (\n" +
            "\t\tselect sac.type as type,sacd.name as name, sacd.id as sacdId\n" +
            "\t\tfrom sa_animal_category sac\n" +
            "\t\tjoin sa_animal_category_details sacd\n" +
            "\t\ton sac.id = sacd.sa_animal_category_id\n" +
            "\t) category\n" +
            "\ton saa.sa_animal_category_details_id = category.sacdId")
    List<AnimalAdopt> queryAll();


    @Select("SELECT saa.id, saa.title, saa.sex, saa.sa_county_id as countyId, saa.amount, saa.age, saa.message, saa.person_name, saa.delete_status, \n" +
            "\tsaa.we_chat, saa.phone, saa.create_time, saa.sa_animal_status_id as statusId, saa.sa_animal_category_details_id as categoryId, \n" +
            "\tsaa.sa_user_id as userId, saa.verify_status \n" +
            "FROM sa_animal_adopt saa\n" +
            "INNER JOIN (SELECT DISTINCT id FROM sa_animal_category_details WHERE sa_animal_category_id = #{categoryId}) saacd\n" +
            "\tON saa.sa_animal_category_details_id = saacd.id\n" +
            "WHERE sa_county_id = #{countyId}")
    List<AnimalAdopt> queryAdoptByCategoryIdAndCountyId(Long countyId, Integer categoryId);

    @Select("SELECT id, title, sex, sa_county_id as countyId, amount, age, message, person_name, delete_status, \n" +
            "we_chat, phone, create_time, sa_animal_status_id as statusId, sa_animal_category_details_id as categoryId, sa_user_id as userId, verify_status \n" +
            "FROM sa_animal_adopt\n" +
            "WHERE sa_county_id \n" +
            "\tIN (\n" +
            "\t\tSELECT id FROM sa_county WHERE sa_city_id = #{cityId}\n" +
            "\t)\n" +
            "AND  delete_status = 0\n" +
            "AND  sa_animal_category_details_id \n" +
            "IN (\n" +
            "\tSELECT id\n" +
            "\tFROM sa_animal_category_details\n" +
            "\tWHERE sa_animal_category_id = #{categoryId}) " +
            "")
    List<AnimalAdopt> queryAdoptByCityIdAndCategoryId(Long cityId, Integer categoryId);


    @Select("SELECT id, title, sex, sa_county_id as countyId, amount, age, message, person_name, delete_status, \n" +
            "we_chat, phone, create_time, sa_animal_status_id as statusId, sa_animal_category_details_id as categoryId, sa_user_id as userId, verify_status\n" +
            "FROM sa_animal_adopt\n" +
            "WHERE sa_user_id = #{userId} AND delete_status = 0")
    List<AnimalAdopt> queryAdoptByUserId(Long userId);

    @Update("UPDATE sa_animal_adopt SET delete_status = 1 WHERE id = #{id}")
    Integer removeAnimalAdoptById(Long id);

    @Update("UPDATE sa_animal_adopt SET delete_status = 0 WHERE id = #{id}")
    int rollbackMultipleAdoptById(Long id);

}
