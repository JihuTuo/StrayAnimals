package com.shiep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sa_report_comment")
public class ReportComment {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("sa_comment_user_id")
    private Long commentUserId;
    private String content;
    private String commentTime;
    @TableField("sa_report_id")
    private Long reportId;
    private Integer deleteStatus;
}
