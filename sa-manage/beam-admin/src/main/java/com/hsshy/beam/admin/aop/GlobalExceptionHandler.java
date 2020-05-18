package com.hsshy.beam.admin.aop;
import com.hsshy.beam.common.aop.BaseControllerExceptionHandler;
import com.hsshy.beam.common.constant.RetEnum;
import com.hsshy.beam.common.utils.R;
import org.apache.shiro.authz.UnauthorizedException;
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
        log.error("异常信息：{}",e.getMessage());
        return R.fail(e.getMessage());
    }


    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R exception(IllegalStateException e) {
        e.printStackTrace();
        log.error("异常信息：{}",e.getMessage());
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R exception(BindException e) {
        e.printStackTrace();
        log.error("异常信息：{}",e.getMessage());
        return R.fail(e.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R exception(MethodArgumentNotValidException e) {
        e.printStackTrace();
        log.error("异常信息：{}",e.getMessage());
        return R.fail(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }


    /**
     * 拦截权限异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R exception(UnauthorizedException e) {
        e.printStackTrace();
        log.error("异常信息：{}",e.getMessage());
        return R.fail(RetEnum.FORBID.getRet(),e.getMessage());
    }




    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public R notFount(RuntimeException e) {
        e.printStackTrace();
        log.error("异常信息：{}",e.getMessage());
        return R.fail(RetEnum.SERVER_EXCEPTION.getRet(), RetEnum.SERVER_EXCEPTION.getMsg());
    }



}
