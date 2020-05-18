package com.shiep.vo;

import com.shiep.entity.ReportComment;
import com.shiep.entity.User;
import lombok.Data;

@Data
public class ReportCommentVo {
    private ReportComment reportComment;
    private User user;
}
