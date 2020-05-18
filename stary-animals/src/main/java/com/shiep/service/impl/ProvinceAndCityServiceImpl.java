package com.shiep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiep.mapper.ICityMapper;
import com.shiep.mapper.ICountyMapper;
import com.shiep.mapper.IProvinceMapper;
import com.shiep.entity.City;
import com.shiep.entity.County;
import com.shiep.entity.Province;
import com.shiep.service.IProvinceAndCityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProvinceAndCityServiceImpl implements IProvinceAndCityService {
    @Resource
    private IProvinceMapper provinceMapper;

    @Resource
    private ICityMapper cityMapper;

    @Resource
    private ICountyMapper countyMapper;

    @Override
    public List<Province> queryAllProvince() {
        return this.provinceMapper.selectList(null);
    }

    @Override
    public List<City> queryCityByProVinceId(Long provinceId) {
        if (provinceId == null) {
            return null;
        }
        QueryWrapper<City> qw = new QueryWrapper<>();
        qw.lambda().eq(City::getProvinceId, provinceId);
        return this.cityMapper.selectList(qw);
    }

    @Override
    public List<County> queryCountyByCityId(Long cityId) {
        System.out.println(cityId + "cityId===") ;
        if (cityId == null) {
            return null;
        }
        QueryWrapper<County> qw = new QueryWrapper<>();
        qw.lambda().eq(County::getCityId, cityId);
        return this.countyMapper.selectList(qw);
    }
}
