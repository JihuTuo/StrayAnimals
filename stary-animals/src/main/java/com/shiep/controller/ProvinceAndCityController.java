package com.shiep.controller;

import com.shiep.entity.City;
import com.shiep.entity.County;
import com.shiep.entity.Province;
import com.shiep.service.impl.ProvinceAndCityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping(value = "provinceAndCity")
@Controller
public class ProvinceAndCityController {
    @Autowired
    private ProvinceAndCityServiceImpl pcServiceImpl;

    @GetMapping(value = "getAllProvince")
    public ResponseEntity<List<Province>> queryAllProvince() {
        List<Province> provinces = this.pcServiceImpl.queryAllProvince();
        if (CollectionUtils.isEmpty(provinces)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(provinces);
    }

    @GetMapping(value = "getCityByProvinceId")
    public ResponseEntity<List<City>> queryCityByProvinceId(
            @RequestParam(value = "provinceId") Long provinceId) {
        List<City> cities = this.pcServiceImpl.queryCityByProVinceId(provinceId);
        if (CollectionUtils.isEmpty(cities)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cities);
    }

    @GetMapping(value = "getCountyByCityId")
    public ResponseEntity<List<County>> queryCountyByCityId(@RequestParam(value = "cityId") Long cityId) {
        System.out.println("==========cityID" + cityId) ;
        List<County> counties = this.pcServiceImpl.queryCountyByCityId(cityId);
        if (CollectionUtils.isEmpty(counties)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(counties);
    }
}
