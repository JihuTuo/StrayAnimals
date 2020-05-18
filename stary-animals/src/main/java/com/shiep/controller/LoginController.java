package com.shiep.controller;

import com.shiep.entity.User;
import com.shiep.jwt.config.JwtConfig;
import com.shiep.jwt.util.CookieUtils;
import com.shiep.mapper.IUserMapper;
import com.shiep.service.impl.AuthServiceImpl;
import com.shiep.service.impl.UserServiceImpl;
import com.shiep.util.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/auth")
@EnableConfigurationProperties(JwtConfig.class)  // 读取配置类中的信息
public class LoginController {

    @Resource
    private UserServiceImpl userService;

    @Autowired
    private AuthServiceImpl authService;

    @Resource
    private IUserMapper userMapper;

    @Resource
    private JwtConfig jwtConfig ;

    @PostMapping("/login")  // http://api.sa.com//stary-animals/login/login
    @ResponseBody    // 此时接收的参数是User，而前端传过来的时候将json转化为了字符串的形式，直接接收即可
    public ResponseEntity<String> login(User user, Integer type, HttpServletRequest request,
                                       HttpServletResponse response) {

        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(user.getUsername());
        boolean isMail = matcher.matches();
        if (!isMail) {
            // 首先设置密码转换
            // 首先根据用户名查询出对应的盐
            User userInDb = this.userService.queryUserByUsername(user.getUsername());
            if (userInDb == null) {
                return ResponseEntity.notFound().build();
            }
            String salt = userInDb.getSalt();
            // 获取当前用户密码加盐后的MD5密码
            String password = CodecUtils.md5Hex(user.getPassword(), salt);

            // 然后和数据库总的对比
            if (StringUtils.equals(password, userInDb.getPassword())) {
                String token = this.saveInToken(userInDb, request, response);
                return ResponseEntity.ok(token);  // 200
            }
            return ResponseEntity.notFound().build();  // 200
        } else {
            User userInDb = this.userService.queryByEmail(user.getUsername());
            String salt = userInDb.getSalt();
            // 获取当前用户密码加盐后的MD5密码
            String password = CodecUtils.md5Hex(user.getPassword(), salt);

            // 然后和数据库总的对比
            if (StringUtils.equals(password, userInDb.getPassword())) {
                String token = this.saveInToken(userInDb, request, response);
                return ResponseEntity.ok(token);  // 200
            }
            return ResponseEntity.notFound().build();  // 200
        }
        // 如果是全是数字，即使用手机登录
        /*if(matches) {
            User userInfo = this.userService.findByPhoneAndPwd(user);
            if (userInfo != null) {
                // 将信息保存到token
                String token = this.saveInToken(userInfo, request, response);
                return ResponseEntity.ok(token);  // 200
            }
            return  ResponseEntity.status(HttpStatus.ACCEPTED).build(); *//*返回201*//*
        }*/


        /*User userInfo = this.userService.findByNameAndPwd(user);
        if (userInfo != null) {
            // 将信息保存到token
            String token = this.saveInToken(userInfo, request, response);
            return ResponseEntity.ok(token);  // 200
        }
        return  ResponseEntity.status(HttpStatus.ACCEPTED).build(); *//*返回201*/
    }


    /**
     * 将用户信息保存在Token中
     * @param
     * @return
     */
    public String saveInToken(User user, HttpServletRequest request,
                            HttpServletResponse response) {
        String token = this.authService.accredit(user.getUsername(), user.getPassword());
//        if (StringUtils.isNotBlank(token)) {
//            CookieUtils.setCookie(request, response, this.jwtConfig.getCookieName(), token, (int) this.jwtConfig.getExpire());
//        }
        if (StringUtils.isNotBlank(token)){
            return token;
        }
        return null;
    }


}
