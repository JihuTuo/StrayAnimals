package com.hsshy.beam.admin.modular.sys.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hsshy.beam.web.modular.base.entity.RestEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;


/**
 * 登陆日志
 *
 * @author hs
 */
@Data
@NoArgsConstructor
@TableName("sys_login_log")
public class LoginLog extends RestEntity<Long> {

    //
    @TableId
    private Long id;
    // 日志名称
    @TableField(value = "log_name")
    private String logName;
    // 用户ID
    @TableField(value = "user_id")
    private Long userId;
    // 是否成功
    @TableField(value = "succeed")
    private String succeed;
    // 消息
    @TableField(value = "message")
    private String message;
    // ip
    @TableField(value = "ip_address")
    private String ipAddress;

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