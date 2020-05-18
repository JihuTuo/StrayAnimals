package com.shiep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysql.jdbc.CacheAdapter;
import com.shiep.entity.Report;
import com.shiep.entity.ReportType;
import com.shiep.entity.User;
import com.shiep.mapper.ILocationDao;
import com.shiep.mapper.IReportMapper;
import com.shiep.mapper.IReportTypeMapper;
import com.shiep.mapper.IUserMapper;
import com.shiep.service.IReportService;
import com.shiep.service.IUserService;
import com.shiep.vo.AnimalSearchVo;
import com.shiep.vo.Location;
import com.shiep.vo.ReportVo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements IReportService {
    @Resource
    private IReportTypeMapper reportTypeMapper;

    @Resource
    private IReportMapper reportMapper;

    @Resource
    private IUserMapper userMapper;

    @Resource
    private ILocationDao locationDao;

    @Override
    public List<ReportType> queryAllReportType() {
        List<ReportType> reportTypes = this.reportTypeMapper.selectList(null);
        if (CollectionUtils.isEmpty(reportTypes)) {
            return null;
        }
        return reportTypes;
    }

    @Override
    public boolean saveReport(Report report) {
        if (report == null) {
            return false;
        }
        return  this.reportMapper.insert(report) > 0 ;
    }

    @Override
    public List<ReportVo> queryReportByTypeOrTimeInPage(Integer typeId, Integer timeType, Long cityId) {
        List<Report> reports = null;
        // 1、）什么都没选的时候
       if (typeId == null && timeType == null && cityId == null) {
            QueryWrapper<Report> qw = new QueryWrapper<>();
             // 没有被删除，并且审核通过
            qw.lambda().eq(Report::getDeleteStatus, 0).eq(Report::getVerifyStatus, 1);
            reports = this.reportMapper.selectList(qw);
            List<ReportVo> reportVos = this.bubildReportVo(reports);
            if (CollectionUtils.isEmpty(reportVos)) {
                return null;
            }
            return reportVos;
        }
       // 1-1、)没选城市的时候选了类型
        if (typeId != null && timeType == null && cityId == null) {
            QueryWrapper<Report> qw = new QueryWrapper<>();
            // 没有被删除，并且审核通过
            qw.lambda().eq(Report::getDeleteStatus, 0).eq(Report::getVerifyStatus, 1)
                        .eq(Report::getTypeId, typeId);
            reports = this.reportMapper.selectList(qw);
            List<ReportVo> reportVos = this.bubildReportVo(reports);
            if (CollectionUtils.isEmpty(reportVos)) {
                return null;
            }
            return reportVos;
        }
        // 1-2、)没选城市的时候选了时间段
        if (typeId == null && timeType != null && cityId == null) {
            switch (timeType) {
                case 1 : {
                    reports = this.reportMapper.queryReportInDay();
                    List<ReportVo> reportVos = this.bubildReportVo(reports);
                    if (CollectionUtils.isEmpty(reportVos)) {
                        return null;
                    }
                    return reportVos;
                }
                case 2 : {
                    reports = this.reportMapper.queryReportInWeek();
                    List<ReportVo> reportVos = this.bubildReportVo(reports);
                    if (CollectionUtils.isEmpty(reportVos)) {
                        return null;
                    }
                    return reportVos;
                }
                case 3 : {
                    reports = this.reportMapper.queryReportInMonth();
                    List<ReportVo> reportVos = this.bubildReportVo(reports);
                    if (CollectionUtils.isEmpty(reportVos)) {
                        return null;
                    }
                    return reportVos;
                }
            }
        }
        // 1-4、）没选城市的时候选了分类和时间段
        if (typeId != null && timeType != null && cityId == null) {
            switch (timeType) {
                case 1 : {
                    reports = this.reportMapper.queryReportByTypeIdAndInDay(typeId);
                    List<ReportVo> reportVos = this.bubildReportVo(reports);
                    if (CollectionUtils.isEmpty(reportVos)) {
                        return null;
                    }
                    return reportVos;
                }
                case 2 : {
                    reports = this.reportMapper.queryReportByTypeIdAndInWeek(typeId);
                    List<ReportVo> reportVos = this.bubildReportVo(reports);
                    if (CollectionUtils.isEmpty(reportVos)) {
                        return null;
                    }
                    return reportVos;
                }
                case 3 : {
                    reports = this.reportMapper.queryReportByTypeIdAndInMonth(typeId);
                    List<ReportVo> reportVos = this.bubildReportVo(reports);
                    if (CollectionUtils.isEmpty(reportVos)) {
                        return null;
                    }
                    return reportVos;
                }
            }
        }

       // 2、）选了城市以后
        if (cityId != null && typeId == null && timeType == null) {
            // 首先过滤城市
            QueryWrapper<Report> qw = new QueryWrapper<>();
            qw.lambda().eq(Report::getDeleteStatus, 0).eq(Report::getVerifyStatus, 1)
                    .eq(Report::getCityId, cityId);
            reports = this.reportMapper.selectList(qw);
            List<ReportVo> reportVos = this.bubildReportVo(reports);
            if (CollectionUtils.isEmpty(reportVos)) {
                return null;
            }
            return reportVos;
        }
        // 3、)选了城市以后，有点了分类
        if (cityId != null && typeId != null && timeType == null) {
            QueryWrapper<Report> qw = new QueryWrapper<>();
            qw.lambda().eq(Report::getDeleteStatus, 0).eq(Report::getVerifyStatus, 1)
                    .eq(Report::getCityId, cityId).eq(Report::getTypeId, typeId);
            reports = this.reportMapper.selectList(qw);
            List<ReportVo> reportVos = this.bubildReportVo(reports);
            if (CollectionUtils.isEmpty(reportVos)) {
                return null;
            }
            return reportVos;
        }
        // 4、）选了城市以后，点了时间
        if(cityId != null && typeId == null && timeType != null) {
            switch (timeType) {
                // 一天内的
                case 1 : {
                    reports = this.reportMapper.queryReportByCityIdAndInDay(cityId);
                    List<ReportVo> reportVos = this.bubildReportVo(reports);
                    if (CollectionUtils.isEmpty(reportVos)) {
                        return null;
                    }
                    return reportVos;
                }
                case 2 : {
                    reports = this.reportMapper.queryReportByCityIdAndInWeek(cityId);
                    List<ReportVo> reportVos = this.bubildReportVo(reports);
                    if (CollectionUtils.isEmpty(reportVos)) {
                        return null;
                    }
                    return reportVos;
                }
                case 3 : {
                    reports = this.reportMapper.queryReportByCityIdAndInMonth(cityId);
                    List<ReportVo> reportVos = this.bubildReportVo(reports);
                    if (CollectionUtils.isEmpty(reportVos)) {
                        return null;
                    }
                    return reportVos;
                }
            }
        }
        // 4、）选了地区后，有点了分类和时间
        if (cityId != null && typeId != null && timeType != null) {
            switch (timeType) {
                case 1 : {
                    reports = this.reportMapper.queryReportByCityIdAndTypeIdAndInDay(cityId, typeId);
                    List<ReportVo> reportVos = this.bubildReportVo(reports);
                    if (CollectionUtils.isEmpty(reportVos)) {
                        return null;
                    }
                    return reportVos;
                }
                case 2 : {
                    reports = this.reportMapper.queryReportByCityIdAndTypeIdAndInWeek(cityId, typeId);
                    List<ReportVo> reportVos = this.bubildReportVo(reports);
                    if (CollectionUtils.isEmpty(reportVos)) {
                        return null;
                    }
                    return reportVos;
                }
                case 3 : {
                    reports = this.reportMapper.queryReportByCityIdAndTypeIdAndInMonth(cityId, typeId);
                    List<ReportVo> reportVos = this.bubildReportVo(reports);
                    if (CollectionUtils.isEmpty(reportVos)) {
                        return null;
                    }
                    return reportVos;
                }
            }
        }
        return null;
    }

    @Override
    public ReportVo queryReportById(Long id) {
        ReportVo vo = new ReportVo();
        if (id == null) {
            return null;
        }
        QueryWrapper<Report> qwReport = new QueryWrapper<>();
        qwReport.lambda().eq(Report::getVerifyStatus, 1).eq(Report::getDeleteStatus, 0)
                    .eq(Report::getId, id);
        Report report = this.reportMapper.selectOne(qwReport);
        vo.setReport(report);

        QueryWrapper<ReportType> qwType = new QueryWrapper<>();
        qwType.lambda().eq(ReportType::getId, report.getTypeId());
        vo.setReportType(this.reportTypeMapper.selectOne(qwType));

        QueryWrapper<User> qwUser = new QueryWrapper<>();
        qwUser.lambda().eq(User::getId, report.getUserId());
        vo.setUser(this.userMapper.selectOne(qwUser));

        Location location = this.locationDao.getLocationByCityId(report.getCityId());
        vo.setLocation(location);

        return vo;
    }

    public List<ReportVo> bubildReportVo(List<Report> reports) {
        List<ReportVo> reportVos = new ArrayList<>();
        if (CollectionUtils.isEmpty(reports)) {
            return null;
        }

        for(Report report : reports) {
            ReportVo vo = new ReportVo();

            vo.setReport(report);

            QueryWrapper<ReportType> qwType = new QueryWrapper<>();
            qwType.lambda().eq(ReportType::getId, report.getTypeId());
            vo.setReportType(this.reportTypeMapper.selectOne(qwType));

            QueryWrapper<User> qwUser = new QueryWrapper<>();
            qwUser.lambda().eq(User::getId, report.getUserId());
            vo.setUser(this.userMapper.selectOne(qwUser));

            Location location = this.locationDao.getLocationByCityId(report.getCityId());
            vo.setLocation(location);

            reportVos.add(vo);
        }
        return reportVos;
    }
}
