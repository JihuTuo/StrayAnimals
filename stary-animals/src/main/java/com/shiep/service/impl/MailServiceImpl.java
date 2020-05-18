package com.shiep.service.impl;

import com.shiep.service.IMailService;
import com.shiep.util.NumberUtils;
import com.shiep.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MailServiceImpl implements IMailService {
    @Resource
    private JavaMailSender javaMailSender ;

    @Resource
    private RedisUtil redisUtil;


    @Override
    public Boolean sendMailAndGenerateCode(String mailAddress) {
        // 邮箱正则
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(mailAddress);
        boolean isMatched = matcher.matches();
        // null或者不是邮箱
        if (StringUtils.isBlank(mailAddress) || !isMatched) {
            return false;
        }
        // 生成随机的六位验证码
        String code = NumberUtils.generateCode(6);
        // 将code存入到Redis中
        boolean flag = this.redisUtil.set(mailAddress, code, 60 / 1000);
        if (!flag) {
            return false;
        }
        /*try {*/
            SimpleMailMessage message = new SimpleMailMessage() ;
            message.setFrom("tuojihu@qq.com");  // 你自己的邮箱地址
            message.setTo(mailAddress); //接受者的邮箱
            message.setSubject("欢迎注册【SHIEP】流浪动物救助平台");
            message.setText("您的注册验证码是：" + "【" + code + "】, " + "验证码的有效时间为60秒！");
            this.javaMailSender.send(message);
      /*  } catch (Exception e) {
           e.printStackTrace();
           return false;
        }*/
        return true;
    }

    @Override
    public String generateCodeFromRedis(String mail) {
        if (StringUtils.isBlank(mail)) {
            return null;
        }
        String code = this.redisUtil.get(mail).toString();
        return code;
    }
}
