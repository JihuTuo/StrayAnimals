package com.hsshy.beam.web.modular.base.dto;

import java.util.List;

/**
 * @description: element-ui树形结构数据格式
 * @author: hs
 * @create: 2018-12-19 09:43:16
 **/
public class TreeNode {

    private Long id;

    private Long parentId;

    private String label;

    private List<TreeNode> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
}
