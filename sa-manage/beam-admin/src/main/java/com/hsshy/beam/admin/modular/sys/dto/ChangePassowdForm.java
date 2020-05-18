package com.hsshy.beam.admin.modular.sys.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description: 登陆实体类
 * @author: hs
 * @create: 2018-10-31 15:45:10
 **/
@Data
public class ChangePassowdForm {

    @NotNull(message = "旧密码不能为空")
    private String oldPwd;
    @NotNull(message = "新密码不能为空")
    private String newPwd;
    @NotNull(message = "再次输入的新密码不能为空")
    private String password_confirm;
}
