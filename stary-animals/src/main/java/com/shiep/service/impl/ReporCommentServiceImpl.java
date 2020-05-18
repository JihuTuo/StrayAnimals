package com.shiep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiep.entity.ReportComment;
import com.shiep.entity.User;
import com.shiep.mapper.IReportCommentMapper;
import com.shiep.mapper.IUserMapper;
import com.shiep.service.IReportCommentService;
import com.shiep.vo.ReportCommentVo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReporCommentServiceImpl implements IReportCommentService {
    @Resource
    private IReportCommentMapper reportCommentMapper;

    @Resource
    private IUserMapper userMapper;

    @Override
    public Boolean saveReportComment(ReportComment reportComment) {
        if (reportComment == null) {
            return null;
        }
        return this.reportCommentMapper.insert(reportComment) > 0 ;
    }

    @Override
    public List<ReportCommentVo> queryReportCommentByReportId(Long reportId) {
        List<ReportCommentVo> reportCommentVos = new ArrayList<>();
        if (reportId == null) {
            return null;
        }
        QueryWrapper<ReportComment> qw = new QueryWrapper<>();
        qw.lambda().eq(ReportComment::getDeleteStatus, 0).eq(ReportComment::getReportId, reportId);
        List<ReportComment> reportComments = this.reportCommentMapper.selectList(qw);
        if (CollectionUtils.isEmpty(reportComments)) {
            return null;
        }
        for (ReportComment reportComment : reportComments) {
            ReportCommentVo vo = new ReportCommentVo();
            vo.setReportComment(reportComment);

            QueryWrapper<User> qwUser = new QueryWrapper<>();
            qwUser.lambda().eq(User::getDeleteStatus, 0).eq(User::getId, reportComment.getCommentUserId());
            vo.setUser(this.userMapper.selectOne(qwUser));

            reportCommentVos.add(vo);
        }
        return reportCommentVos;
    }

    @Override
    public ReportCommentVo ReportCommentByCommentId(Long commentId) {
        if (commentId == null) {
            return null;
        }
        QueryWrapper<ReportComment> qw = new QueryWrapper<>();
        qw.lambda().eq(ReportComment::getDeleteStatus, 0).eq(ReportComment::getId, commentId);
        ReportComment reportComment = this.reportCommentMapper.selectOne(qw);

        ReportCommentVo vo = new ReportCommentVo();
        vo.setReportComment(reportComment);

        QueryWrapper<User> qwUser = new QueryWrapper<>();
        qwUser.lambda().eq(User::getDeleteStatus, 0).eq(User::getId, reportComment.getCommentUserId());
        vo.setUser(this.userMapper.selectOne(qwUser));

        return vo;
    }

}
