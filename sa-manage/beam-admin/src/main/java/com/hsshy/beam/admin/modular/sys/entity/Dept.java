package com.hsshy.beam.admin.modular.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hsshy.beam.web.modular.base.entity.RestEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * 部门管理
 *
 * @author hs
 */
@Data
@NoArgsConstructor
@TableName("sys_dept")
public class Dept extends RestEntity<Long> {

    //
    @TableId
    private Long id;
    // 上级部门ID，一级部门为0
    @TableField(value = "parent_id")
    private Long parentId;
    // 部门名称
    @TableField(value = "name")
    private String name;
    // 排序
    @TableField(value = "order_num")
    private Integer orderNum;
    // 是否删除  -1：已删除  0：正常
    @TableField(value = "del_flag")
    private Integer delFlag;

    @TableField(exist = false)
    private List<?> children;

    @TableField(exist = false)
    private String label;

    @TableField(exist = false)
    private String pname;


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
