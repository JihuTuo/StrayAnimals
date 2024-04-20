package com.shiep.controller;

import com.shiep.entity.User;
import com.shiep.jwt.config.JwtConfig;
import com.shiep.mapper.IUserMapper;
import com.shiep.service.impl.AuthServiceImpl;
import com.shiep.service.impl.UserServiceImpl;
import com.shiep.util.CodecUtils;
import com.shiep.vo.CommonResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

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

    @PostMapping("/login")
    @ResponseBody    // 此时接收的参数是User，而前端传过来的时候将json转化为了字符串的形式，直接接收即可
    public ResponseEntity<CommonResult> login(User user, Integer type, HttpServletRequest request,
                                              HttpServletResponse response) {
        // 首先根据用户名查询出对应的盐
        User userInDb = this.userService.queryUserByUsername(user.getUsername());
        if (userInDb == null) {
            return ResponseEntity.of(Optional.of(new CommonResult(404)));
        }
        String salt = userInDb.getSalt();
        // 获取当前用户密码加盐后的MD5密码
        String password = CodecUtils.md5Hex(user.getPassword(), salt);

        // 然后和数据库总的对比
        if (StringUtils.equals(password, userInDb.getPassword())) {
            String token = this.saveInToken(userInDb, request, response);
            return ResponseEntity.ok(new CommonResult(200, token));  // 200
        }
        return ResponseEntity.of(Optional.of(new CommonResult(100)));
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
