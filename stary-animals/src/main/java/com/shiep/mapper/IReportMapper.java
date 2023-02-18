package com.shiep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiep.entity.PhotoAnimalSearch;
import com.shiep.entity.Report;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IReportMapper extends BaseMapper<Report> {
    /**
     * 查询一天内
     * @param cityId
     * @return
     */
    @Select("SELECT id, title, content, sa_city_id as cityId, create_time, update_time\n" +
            "\t\t, verify_status, delete_status, sa_report_type_id as typeId\n" +
            "\t\t, sa_user_id as userId\n" +
            "FROM sa_report\n" +
            "WHERE sa_city_id = #{cityId} AND delete_status = 0 AND verify_status = 1\n" +
            "AND DATE_FORMAT(create_time,'%Y-%m-%d') = curdate()")
    List<Report> queryReportByCityIdAndInDay(Long cityId);

    @Select("SELECT id, title, content, sa_city_id as cityId, create_time, update_time\n" +
            "\t\t, verify_status, delete_status, sa_report_type_id as typeId\n" +
            "\t\t, sa_user_id as userId\n" +
            "FROM sa_report WHERE delete_status = 0 AND verify_status = 1\n" +
            "AND  DATE_FORMAT(create_time,'%Y-%m-%d') = curdate()")
    List<Report> queryReportInDay();

    // NOW() - INTERVAL 1 WEEK  获得一周前时间
    @Select("SELECT id, title, content, sa_city_id as cityId, create_time, update_time\n" +
            "\t\t, verify_status, delete_status, sa_report_type_id as typeId\n" +
            "\t\t, sa_user_id as userId\n" +
            "FROM sa_report\n" +
            "WHERE sa_city_id = #{cityId} AND delete_status = 0 AND verify_status = 1\n" +
            "AND create_time > NOW() - INTERVAL 1 WEEK")
    List<Report> queryReportByCityIdAndInWeek(Long cityId);


    @Select("SELECT id, title, content, sa_city_id as cityId, create_time, update_time\n" +
            "\t\t, verify_status, delete_status, sa_report_type_id as typeId\n" +
            "\t\t, sa_user_id as userId\n" +
            "FROM sa_report WHERE delete_status = 0 AND verify_status = 1\n" +
            "AND create_time > NOW() - INTERVAL 1 WEEK")
    List<Report> queryReportInWeek();


    @Select("SELECT id, title, content, sa_city_id as cityId, create_time, update_time\n" +
            "\t\t, verify_status, delete_status, sa_report_type_id as typeId\n" +
            "\t\t, sa_user_id as userId\n" +
            "FROM sa_report\n" +
            "WHERE sa_city_id = #{cityId} AND delete_status = 0 AND verify_status = 1\n" +
            "AND create_time > NOW() - INTERVAL 1 MONTH")
    List<Report> queryReportByCityIdAndInMonth(Long cityId);

    @Select("SELECT id, title, content, sa_city_id as cityId, create_time, update_time\n" +
            "\t\t, verify_status, delete_status, sa_report_type_id as typeId\n" +
            "\t\t, sa_user_id as userId\n" +
            "FROM sa_report WHERE delete_status = 0 AND verify_status = 1\n" +
            "AND create_time > NOW() - INTERVAL 1 MONTH")
    List<Report> queryReportInMonth();


    @Select("SELECT id, title, content, sa_city_id as cityId, create_time, update_time\n" +
            "\t\t, verify_status, delete_status, sa_report_type_id as typeId\n" +
            "\t\t, sa_user_id as userId\n" +
            "FROM sa_report\n" +
            "WHERE sa_city_id = #{cityId} AND sa_report_type_id = #{typeId} AND delete_status = 0 AND verify_status = 1\n" +
            "AND DATE_FORMAT(create_time,'%Y-%m-%d') = curdate()")
    List<Report> queryReportByCityIdAndTypeIdAndInDay(Long cityId, Integer typeId);

    @Select("SELECT id, title, content, sa_city_id as cityId, create_time, update_time\n" +
            "\t\t, verify_status, delete_status, sa_report_type_id as typeId\n" +
            "\t\t, sa_user_id as userId\n" +
            "FROM sa_report\n" +
            "WHERE  sa_report_type_id = #{typeId} AND delete_status = 0 AND verify_status = 1\n" +
            "AND DATE_FORMAT(create_time,'%Y-%m-%d') = curdate()")
    List<Report> queryReportByTypeIdAndInDay(Integer typeId);


    @Select("SELECT id, title, content, sa_city_id as cityId, create_time, update_time\n" +
            "\t\t, verify_status, delete_status, sa_report_type_id as typeId\n" +
            "\t\t, sa_user_id as userId\n" +
            "FROM sa_report\n" +
            "WHERE sa_city_id = #{cityId} AND sa_report_type_id = #{typeId} AND delete_status = 0 AND verify_status = 1\n" +
            "AND create_time > NOW() - INTERVAL 1 WEEK")
    List<Report> queryReportByCityIdAndTypeIdAndInWeek(Long cityId, Integer typeId);

    @Select("SELECT id, title, content, sa_city_id as cityId, create_time, update_time\n" +
            "\t\t, verify_status, delete_status, sa_report_type_id as typeId\n" +
            "\t\t, sa_user_id as userId\n" +
            "FROM sa_report\n" +
            "WHERE  sa_report_type_id = #{typeId} AND delete_status = 0 AND verify_status = 1\n" +
            "AND create_time > NOW() - INTERVAL 1 WEEK")
    List<Report> queryReportByTypeIdAndInWeek(Integer typeId);



    @Select("SELECT id, title, content, sa_city_id as cityId, create_time, update_time\n" +
            "\t\t, verify_status, delete_status, sa_report_type_id as typeId\n" +
            "\t\t, sa_user_id as userId\n" +
            "FROM sa_report\n" +
            "WHERE sa_city_id = #{cityId} AND sa_report_type_id = #{typeId} AND delete_status = 0 AND verify_status = 1\n" +
            "AND create_time > NOW() - INTERVAL 1 MONTH")
    List<Report> queryReportByCityIdAndTypeIdAndInMonth(Long cityId, Integer typeId);

    @Select("SELECT id, title, content, sa_city_id as cityId, create_time, update_time\n" +
            "\t\t, verify_status, delete_status, sa_report_type_id as typeId\n" +
            "\t\t, sa_user_id as userId\n" +
            "FROM sa_report\n" +
            "WHERE  sa_report_type_id = #{typeId} AND delete_status = 0 AND verify_status = 1\n" +
            "AND create_time > NOW() - INTERVAL 1 MONTH")
    List<Report> queryReportByTypeIdAndInMonth(Integer typeId);


    @Select("SELECT * FROM sa_report")
    List<Report> findAll();
}
