package com.shiep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.shiep.vo.Category;
import com.shiep.vo.Location;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 领养动物
 */
@Data
@TableName( value = "sa_animal_adopt")
public class AnimalAdopt implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String title;
    private String sex;
    @TableField("sa_county_id")
    private Long countyId;
    private Integer amount;
    private Double age;
    private String message;
    private String personName;
    private String weChat;
    private String phone;
    private String createTime;
    private Integer verifyStatus;
    @TableField("sa_animal_status_id")
    private Integer statusId;
    @TableField("sa_animal_category_details_id")
    private Integer categoryId;
    @TableField("sa_user_id")
    private Long userId;
    private Integer deleteStatus;

}
