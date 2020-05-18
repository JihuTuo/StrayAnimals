package com.shiep.mapper;

import com.shiep.vo.Location;
import org.apache.ibatis.annotations.Select;

public interface ILocationDao {
    @Select("SELECT sap.name as province, sac.name as city, saco.name as county\n" +
            "FROM sa_province sap\n" +
            "\tJOIN sa_city sac\n" +
            "\tON sap.id = sac.sa_province_id\n" +
            "\tJOIN  sa_county saco\n" +
            "\tON sac.id = saco.sa_city_id\n" +
            "\tWHERE saco.id = #{countyId}")
    Location getLocationByCountyId(Long countyId);

    @Select("SELECT sap.name as province, sac.name as city, saco.name as county\n" +
            "FROM (  SELECT id, name\n" +
            "\tFROM sa_province \n" +
            "\tWHERE id = \"#{provinceId}\") sap\n" +
            "\tJOIN (\n" +
            "\t\tSELECT id, name, sa_province_id\n" +
            "\t\tFROM sa_city\n" +
            "\t\tWHERE sa_city.id = \"#{cityId}\"\n" +
            "\t) sac\n" +
            "\tON sap.id = sac.sa_province_id\n" +
            "\tJOIN  sa_county saco\n" +
            "\tON sac.id = saco.sa_city_id\n" +
            "\tWHERE saco.id = #{countyId}")
    Location getLocationByProAndCity(Long countyId, Long provinceId, Long cityId);


    @Select("SELECT sp.name as province, sc.name as city\n" +
            "FROM  sa_province sp\n" +
            "\tJOIN \n" +
            "\t\t(SELECT name, sa_province_id as id  FROM  sa_city  WHERE id  = #{cityId}) sc\n" +
            "\tON sp.id = sc.id")
    Location getLocationByCityId(Long cityId);
}
