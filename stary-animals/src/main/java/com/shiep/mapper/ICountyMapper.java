package com.shiep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiep.entity.County;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ICountyMapper extends BaseMapper<County> {

    @Select("SELECT id\n" +
            "FROM sa_county\n" +
            "WHERE sa_city_id = #{cityId}")
    List<Long> getIdByCityId(Long cityId);
}
