package com.shiep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiep.entity.AnimalSearchComment;
import org.apache.ibatis.annotations.Update;

import java.beans.Transient;

public interface IAnimalSearchCommentMapper extends BaseMapper<AnimalSearchComment> {
    @Update("UPDATE sa_animal_search_comment SET delete_status = 1 WHERE id = #{id}")
    @Transient
    int deleteCommentById(Long id);

    @Update("UPDATE sa_animal_search_comment SET delete_status = 0 WHERE id = #{id}")
    @Transient
    int rollbackCommentById(Long id);
}
