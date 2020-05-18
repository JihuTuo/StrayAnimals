package com.shiep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiep.entity.News;
import org.apache.ibatis.annotations.Update;

public interface INewsMapper extends BaseMapper<News> {

    @Update("UPDATE sa_news SET delete_status = 1 WHERE id = #{id}")
    int deleteNewsById(Long id);


    @Update("UPDATE sa_news SET delete_status = 0 WHERE id = #{idd}")
    int rollbackNewsById(Long id);
}
