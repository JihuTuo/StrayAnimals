package com.shiep.controller;

import com.shiep.entity.ResultMessage;
import com.shiep.jwt.config.JwtConfig;
import com.shiep.jwt.pojo.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class TokenController {
 
    @Resource
    private JwtConfig jwtConfig ;
 
    /**
     * 用户登录接口
     * @param userName
     * @param passWord
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestParam("userName") String userName,
                                    @RequestParam("passWord") String passWord){

        // 这里模拟通过用户名和密码，从数据库查询userId
        // 这里把userId转为String类型，实际开发中如果subject需要存userId，则可以JwtConfig的createToken方法的参数设置为Long类型
        String userId = 5 + "";
        // 生成token
        String token = jwtConfig.createToken(new UserInfo(12345L, "qwerty", "D:\\Tool_Workspace\\IDEA\\sa-front\\img\\cat")) ;
        if (!StringUtils.isEmpty(token)) {
            return ResponseEntity.ok(token);
        }
        return null;
    }
 
    /**
     * 需要 Token 验证的接口
     */
//    @PostMapping("/info")
//    public ResultBO<?> info (){
//        return ResultTool.success("info") ;
//    }
 
    /**
     * 根据请求头的token获取userId
     * @param request
     * @return
     */
    @GetMapping("/getUserInfo")
    public ResultMessage getUserInfo(HttpServletRequest request){
        UserInfo userInfo = jwtConfig.getUserInfoFromToken(request.getHeader("token"));
        return ResultMessage.ok(userInfo);
    }
 
    /*
        为什么项目重启后，带着之前的token还可以访问到需要info等需要token验证的接口？
        答案：只要不过期，会一直存在，类似于redis
     */


    /*---------------------------------------------------------------------------*/

}