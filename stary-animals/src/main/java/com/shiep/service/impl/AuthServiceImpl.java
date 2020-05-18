package com.shiep.service.impl;

import com.shiep.entity.User;
import com.shiep.jwt.config.JwtConfig;
import com.shiep.jwt.pojo.UserInfo;
import com.shiep.mapper.IUserMapper;
import com.shiep.service.IAuthService;
import com.shiep.service.IMailService;
import com.shiep.util.CodecUtils;
import com.shiep.util.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.beans.Transient;

@Service
public class AuthServiceImpl implements IAuthService {
    @Resource
    private UserServiceImpl userService;
    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private IUserMapper userMapper;

    @Resource
    private JavaMailSender javaMailSender;

    @Override
    public String accredit(String username, String password) {
        // 判断用户名和密码是否正确
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return null;
        }
        User user = userService.findByNameAndPwd(username, password);
        if (user == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setPhoto(user.getPhoto());
        String token = this.jwtConfig.createToken(userInfo);
        return token;
    }

    @Transient
    @Override
    public boolean resetPassword(String username, String email) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(email)) {
            return false;
        }
        User user = this.userService.queryUserByNameAndEmail(username, email);
        if (user == null) {
            return false;
        }

        String code = NumberUtils.generateCode(6);
        String newPwd = "shiep" + code;

        String salt = CodecUtils.generateSalt();
        String newPasswordInDb = CodecUtils.md5Hex(newPwd, salt);

        user.setPassword(newPasswordInDb);

        int update = this.userMapper.update(user, null);

        if (update <= 0) {
            return false;
        }

        // 发送邮件

       try {
           SimpleMailMessage message = new SimpleMailMessage() ;
           message.setFrom("tuojihu@qq.com");  // 你自己的邮箱地址
           message.setTo(email); //接受者的邮箱
           message.setSubject("重置密码 【SHIEP】流浪动物救助平台");
           message.setText("您重置后的新密码是：" + "【" + newPwd + "】");
           this.javaMailSender.send(message);
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
        return true;
    }


}
