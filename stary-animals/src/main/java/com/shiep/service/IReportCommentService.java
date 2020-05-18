package com.shiep.service;

import com.shiep.entity.ReportComment;
import com.shiep.vo.ReportCommentVo;

import java.util.List;

public interface IReportCommentService {

    Boolean saveReportComment(ReportComment reportComment);

    List<ReportCommentVo> queryReportCommentByReportId(Long reportId);

    ReportCommentVo ReportCommentByCommentId(Long commentId);
}
