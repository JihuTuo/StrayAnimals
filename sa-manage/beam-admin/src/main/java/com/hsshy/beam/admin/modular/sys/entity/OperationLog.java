package com.hsshy.beam.admin.modular.sys.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hsshy.beam.web.modular.base.entity.RestEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 操作日志
 *
 * @author hs
 */
@Data
@NoArgsConstructor
@TableName("sys_operation_log")
public class OperationLog extends RestEntity<Long> {

    //
    @TableId
    private Long id;
    // 日志类型
    @TableField(value = "log_type")
    private String logType;
    // 日志名称
    @TableField(value = "log_name")
    private String logName;
    // 用户ID
    @TableField(value = "user_id")
    private Long userId;
    // 类名称
    @TableField(value = "class_name")
    private String className;
    // 方法名称
    @TableField(value = "method")
    private String method;
    // 是否成功
    @TableField(value = "succeed")
    private String succeed;
    // 备注
    @TableField(value = "message")
    private String message;
    @TableField(exist = false)
    private String userName;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}