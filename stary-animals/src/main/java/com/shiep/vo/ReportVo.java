package com.shiep.vo;

import com.shiep.entity.Report;
import com.shiep.entity.ReportType;
import com.shiep.entity.User;
import lombok.Data;

@Data
public class ReportVo {
    private Report report;
    private ReportType reportType;
    private User user;
    private Location location;
}
