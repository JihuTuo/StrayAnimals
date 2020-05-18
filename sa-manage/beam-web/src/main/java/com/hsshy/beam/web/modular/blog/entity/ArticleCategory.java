package com.hsshy.beam.web.modular.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hsshy.beam.web.modular.base.entity.RestEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author hs
 */
@Data
@NoArgsConstructor
@TableName("b_article_category")
public class ArticleCategory extends RestEntity<Long> {

    //
    @TableId
    private Long id;
    // 分类名称
    @TableField(value = "name")
    private String name;
    // 是否可用
    @TableField(value = "frozen")
    private Integer frozen;
    //排序
    @TableField(value = "sort")
    private Integer sort;
    //文章数量
    @TableField(exist = false)
    private Integer articleCount;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}