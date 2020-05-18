package com.hsshy.beam.admin.modular.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hsshy.beam.web.modular.base.entity.RestEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 字典表
 *
 * @author hs
 */
@Data
@NoArgsConstructor
@TableName("sys_dict")
public class Dict extends RestEntity<Long> {

    // 主键id
    @TableId
    private Long id;
    // 父级字典
    @TableField(value = "pid")
    private Long pid;
    // 名称
    @TableField(value = "name")
    private String name;
    // 描述
    @TableField(value = "des")
    private String des;
    // 编码
    @TableField(value = "code")
    private String code;
    // 排序
    @TableField(value = "sort")
    private Integer sort;
    // 编码
    @TableField(exist = false)
    private String keyword;

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