package com.shiep.service;

import com.shiep.entity.Report;
import com.shiep.entity.ReportType;
import com.shiep.vo.ReportVo;

import java.beans.Transient;
import java.util.List;

public interface IReportService {

    List<ReportType> queryAllReportType();

    @Transient
    boolean saveReport(Report report);

    @Transient
    List<ReportVo> queryReportByTypeOrTimeInPage(Integer typeId, Integer timeType, Long cityId);

    ReportVo queryReportById(Long id);

}
