package com.hsshy.beam.api.aop;
import com.hsshy.beam.common.aop.BaseControllerExceptionHandler;
import com.hsshy.beam.common.constant.RetEnum;
import com.hsshy.beam.common.utils.R;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
 *
 * @author hs
 * @date 2018年9月19日 下午19:19:56
 */
@ControllerAdvice
public class GlobalExceptionHandler extends BaseControllerExceptionHandler {


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R exception(IllegalArgumentException e) {
        e.printStackTrace();
        return R.fail(e.getMessage());
    }


    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R exception(IllegalStateException e) {
        e.printStackTrace();
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R exception(BindException e) {
        e.printStackTrace();
        return R.fail(e.getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 拦截jwt相关异常
     */
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public R jwtException(JwtException e) {
        e.printStackTrace();
        log.error("jwt相关异常:"+e.getMessage());
        return R.fail(RetEnum.TOKEN_ERROR.getRet(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R exception(MethodArgumentNotValidException e) {
        e.printStackTrace();
        return R.fail(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R exception(Exception e) {
        e.printStackTrace();
        return R.fail(RetEnum.SERVER_EXCEPTION.getRet(),e.getMessage());
    }




}
