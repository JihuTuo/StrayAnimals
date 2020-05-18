package com.shiep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

@Data
@TableName("sa_animal_category")
public class AnimalCategory implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
    private String type;
}
