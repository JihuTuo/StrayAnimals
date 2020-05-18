package com.shiep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sa_animal_search_comment")
public class AnimalSearchComment implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("sa_comment_user_id")
    private Long commentUserId;
    private String content;
    private Integer deleteStatus;
    private String commentTime;
    @TableField("sa_animal_search_id")
    private Long searchId;
    @TableField("sa_comment_user_name")
    private String commentUserName;
}
