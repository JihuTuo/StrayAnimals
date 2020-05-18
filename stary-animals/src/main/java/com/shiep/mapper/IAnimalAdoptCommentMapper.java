package com.shiep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiep.entity.AnimalAdoptComment;
import org.apache.ibatis.annotations.Update;

import java.beans.Transient;

public interface IAnimalAdoptCommentMapper extends BaseMapper<AnimalAdoptComment> {

    @Update("UPDATE sa_animal_adopt_comment SET delete_status = 1 WHERE id = #{id}")
    @Transient
    int deleteCommentById(Long id);

    @Update("UPDATE sa_animal_adopt_comment SET delete_status = 0 WHERE id = #{id}")
    @Transient
    int rollbackCommentById(Long id);
}
