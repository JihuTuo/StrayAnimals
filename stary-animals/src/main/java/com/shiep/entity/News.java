package com.shiep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sa_news")
public class News implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String title;
    private String content;
    private String createTime;
    private Integer deleteStatus;
    @TableField("sa_news_type_id")
    private Integer typeId;
    @TableField("sa_user_id")
    private Long userId;
    private String text;
    private String photo;
}
