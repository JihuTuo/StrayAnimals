package com.shiep.service;

import com.shiep.entity.City;
import com.shiep.entity.County;
import com.shiep.entity.Province;

import java.util.List;

public interface IProvinceAndCityService {
    List<Province> queryAllProvince();

    List<City> queryCityByProVinceId(Long provinceId);

    List<County> queryCountyByCityId(Long cityId);

}
