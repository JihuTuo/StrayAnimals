package com.shiep.jwt.intercepter;

import com.shiep.jwt.config.JwtConfig;
import com.shiep.jwt.pojo.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {
 
    @Resource
    private JwtConfig jwtConfig ;
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws SignatureException {
       //** 地址过滤 *//*
        String uri = request.getRequestURI() ;
        if (uri.contains("/login")
                || uri.contains("/test")
                || uri.contains("/register")
                || uri.contains("/ueditor/config")
                || uri.contains("/ueditor/upload")
                || uri.contains("/auth/resetPassword")
                || uri.contains("/email/generateCode")
                || uri.contains("/provinceAndCity/getCountyByCityId?cityId")
        ){
            return true ;
        }
        //** Token 验证 *//*
        // 从header中获取token，前端发送请求的时候将token保存在header中
        String token = request.getHeader(jwtConfig.getHeader());
        // 如果没有从header中渠道，就从参数中获取
        if(StringUtils.isEmpty(token)){
            token = request.getParameter(jwtConfig.getHeader());
        }
        // 如果从header和参数中都没有获取到，就抛出异常
        if(StringUtils.isEmpty(token)){
            throw new SignatureException(jwtConfig.getHeader()+ "token不能为空");
        }
        // 验证token身份是否正确
        if (!StringUtils.isEmpty(token)) {
            Claims claims = jwtConfig.getTokenClaim(token);
            UserInfo userInfo = this.jwtConfig.getUserInfoFromToken(token);
            System.out.println("拦截器" + claims ) ;
            System.out.println("拦截器" +userInfo) ;
            if (userInfo == null || claims == null || jwtConfig.isTokenExpired(claims.getExpiration())) {
                throw new SignatureException(jwtConfig.getHeader()+ "身份验证未通过！");
            }
        }
        return true;
    }
}
