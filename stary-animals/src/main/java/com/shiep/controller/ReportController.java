package com.shiep.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shiep.entity.AnimalSearch;
import com.shiep.entity.Report;
import com.shiep.entity.ReportComment;
import com.shiep.entity.ReportType;
import com.shiep.mapper.IReportMapper;
import com.shiep.service.IReportCommentService;
import com.shiep.service.IReportService;
import com.shiep.vo.ReportCommentVo;
import com.shiep.vo.ReportVo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("report")
public class ReportController {
    @Resource
    private IReportService reportService;

    @Resource
    private IReportMapper reportMapper;

    @Resource
    private IReportCommentService reportCommentService;


    @GetMapping("getAllReportType")
    private ResponseEntity<List<ReportType>> queryAllReportType() {
        List<ReportType> reportTypes = this.reportService.queryAllReportType();
        if (!CollectionUtils.isEmpty(reportTypes)) {
            return ResponseEntity.ok(reportTypes);
        }
        return null;
    }

    @PostMapping("saveReport")
    private ResponseEntity<Void> saveReport(Report report) {
         if(this.reportService.saveReport(report)) {
             return ResponseEntity.ok().build();
         }
         return null;
    }

    @GetMapping("getReportByTypeOrTimeInPage")
    public  ResponseEntity<PageInfo<ReportVo>> getReportByTypeOrTimeInPage(
            int size, int page, Integer typeId, Integer timeType, Long cityId) {
        Page p = PageHelper.startPage(page , size);
        List<ReportVo> ReportVos = this.reportService.queryReportByTypeOrTimeInPage(typeId, timeType, cityId);
        PageInfo<ReportVo> pageInfo = new PageInfo(ReportVos);
        pageInfo.setPages(p.getPages());//总页数
        pageInfo.setTotal(p.getTotal());//总条数
        if (CollectionUtils.isEmpty(ReportVos)) {
            return null;
        }
        return ResponseEntity.ok(pageInfo);
    }

    @GetMapping("getReportById")
    public ResponseEntity<ReportVo> getReportById(@RequestParam("id")Long  id) {
        ReportVo reportVo = this.reportService.queryReportById(id);
        if(reportVo != null) {
            return ResponseEntity.ok(reportVo);
        }
        return null;
    }


    @PostMapping("saveReportComment")
    public ResponseEntity<Void> saveReportComment(ReportComment reportComment) {
        if(this.reportCommentService.saveReportComment(reportComment)) {
            return ResponseEntity.ok().build();
        }
        return null;
    }


    @GetMapping("getReportCommentByReportId")
    public ResponseEntity<List<ReportCommentVo>> getReportCommentReportId(
                @RequestParam("reportId") Long reportId) {
        List<ReportCommentVo> reportCommentVos = this.reportCommentService.queryReportCommentByReportId(reportId);
        if (!CollectionUtils.isEmpty(reportCommentVos)) {
            return ResponseEntity.ok(reportCommentVos);
        }
        return null;
    }

    @GetMapping("getReportCommentByCommentId")
    public ResponseEntity<ReportCommentVo> queryReportCommentByCommentId(Long commentId) {
        ReportCommentVo reportCommentVo = this.reportCommentService.ReportCommentByCommentId(commentId);
        if (reportCommentVo == null) {
            return null;
        }
        return ResponseEntity.ok(reportCommentVo);
    }

    @PostMapping("saveReportComent")
    public ResponseEntity<Void> saveReportComent(ReportComment reportComment) {
        if(this.reportCommentService.saveReportComment(reportComment)) {
            return ResponseEntity.ok().build();
        }
        return null;
    }
}
