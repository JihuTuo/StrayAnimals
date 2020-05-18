package com.shiep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sa_animal_search")
public class AnimalSearch implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String title;
    private String sex;
    @TableField("sa_county_id")
    private Long countyId;
    private Double age;
    private String message;
    private String personName;
    private String weChat;
    private String phone;
    private String createTime;
    // 审核是否通过
    private Integer verifyStatus;
    @TableField("sa_animal_status_id")
    private Integer statusId;
    @TableField("sa_animal_category_details_id")
    private Integer categoryId;
    @TableField("sa_user_id")
    private Long userId;
    private Integer deleteStatus;
    // 穿的是动物还是主人
    private Integer searchAnimal;
    private Integer money;
}
