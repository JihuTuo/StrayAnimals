package com.shiep.vo;

import com.shiep.entity.City;
import com.shiep.entity.County;
import com.shiep.entity.Province;
import lombok.Data;

import java.io.Serializable;

/**
 * 用来保存地址信息
 */

@Data
public class Location implements Serializable {
    private String province;
    private String city;
    private String county;
}
