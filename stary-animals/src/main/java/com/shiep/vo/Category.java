package com.shiep.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Category implements Serializable {
    // 总分类
    private String type;
    // 二级分类
    private String name;
}
