package com.shiep.controller;

import com.shiep.entity.User;
import com.shiep.jwt.config.JwtConfig;
import com.shiep.jwt.pojo.UserInfo;
import com.shiep.service.IUserService;
import com.shiep.service.impl.AuthServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@EnableConfigurationProperties(JwtConfig.class)  // 读取配置类中的信息
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private JwtConfig jwtConfig;

    @Resource
    private IUserService userService;

    /**
     * 登录授权
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/accredit")
    public ResponseEntity<String> authentication(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response) {
        // 登录校验
        String token = this.authService.accredit(username, password);
        if (StringUtils.isBlank(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
        //CookieUtilsOld.setCookie(request, response, this.jwtConfig.getCookieName(), token, (int) this.jwtConfig.getExpire());
       // CookieUtils.setCookie(request, response, this.jwtConfig.getCookieName(), token, (int) this.jwtConfig.getExpire());
        return ResponseEntity.ok(token);
    }

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(HttpServletRequest request) {
        String token = request.getHeader("token");
        System.out.println(token);
        if (StringUtils.isBlank(token)) {
            return ResponseEntity.badRequest().build();
        }
        UserInfo userInfo = this.jwtConfig.getUserInfoFromToken(token);
        if (userInfo == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(User user) {
        boolean register = this.userService.register(user);
        if (register) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("resetPassword")
    public ResponseEntity<Void> resetPassword(String username, String email) {
        boolean flag = this.authService.resetPassword(username, email);
        if (!flag) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

}