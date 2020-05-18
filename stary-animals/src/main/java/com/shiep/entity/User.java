package com.shiep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("sa_user")  // 表名称与类不同的时候注释
@Data
public class User implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)  // 配置之间生成策略
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String phone;
    private String photo;
    private String createTime;
    private String birthday;
    private Integer sex;
    @TableField("sa_admin_category_id")  // 数据库中字段与Entity不同的时候注释
    private Integer adminType;
    private Integer deleteStatus;
    @JsonIgnore
    private String salt;
}
