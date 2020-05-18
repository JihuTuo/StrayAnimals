package com.hsshy.beam.web.modular.blog.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hsshy.beam.web.modular.base.entity.RestEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

/**
 * @author hs
 */
@Data
@NoArgsConstructor
@TableName("b_article")
public class Article extends RestEntity<Long> {

    //
    @TableId(type = IdType.AUTO)
    private Long id;
    // 标题
    @TableField(value = "title")
    private String title;
    // 作者
    @TableField(value = "author")
    private String author;
    // 内容
    @TableField(value = "content")
    private String content;
    // 是否可用
    @TableField(value = "frozen")
    private Integer frozen;
    //短码
    @TableField(value = "short_code")
    private String shortCode;
    //头像
    @TableField(value = "head_img")
    private String headImg;
    //阅读数
    @TableField(value = "read_num")
    private Integer readNum;
    //排序
    @TableField(value = "sort")
    private Integer sort;
    // 0：markdown 1：富文本
    @TableField(value = "text_type")
    private Integer textType;

    @TableField(value = "tag")
    private String tag;

    @TableField(exist = false)
    private List<Long> cids;
    @TableField(exist = false)
    private Long cid;

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