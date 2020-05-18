package com.shiep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sa_report_type")
public class ReportType {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
    private String type;
}
