package com.shiep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiep.entity.News;
import com.shiep.entity.PhotoAnimalAdopt;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface INewsMapper extends BaseMapper<News> {

    @Update("UPDATE sa_news SET delete_status = 1 WHERE id = #{id}")
    int deleteNewsById(Long id);


    @Update("UPDATE sa_news SET delete_status = 0 WHERE id = #{idd}")
    int rollbackNewsById(Long id);

    @Select("SELECT * FROM sa_news")
    List<News> findAll();
}
