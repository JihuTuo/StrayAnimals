package com.hsshy.beam.common.constant;

/**
 * 返回信息枚举
 * created by ZWen
 * at 2017/12/6 17:49
 */
public enum RetEnum {

    SUCCESS(200,"成功"),

    ERROR(400,"失败"),
    /**
     * 禁止访问
     */
    FORBID(403,"禁止访问"),
    /**
     * 参数异常
     */
    ERROR_PARAM(1004,"参数异常"),
    /**
     * token过期
     */
    TOKEN_EXPIRED(401,"token过期"),
    /**
     * token验证失败
     */
    TOKEN_ERROR(401,"token验证失败"),
    /**
     * 签名异常
     */
    SIGN_ERROR(401, "签名验证失败"),
    /**
     * session过期
     */
    LOGIN_EXPIRED(401,"请重新登陆"),
    /**
     * 其他
     */
    WRITE_ERROR(500, "渲染界面错误"),
    /**
     * 文件上传
     */
    FILE_NOT_FOUND(404, "未找到文件!"),

    FILE_READING_ERROR(500, "文件读取错误!"),

    UPLOAD_ERROR(500,"上传图片出错"),

    CAPTCHA_LAPSED(500,"验证码已失效"),
   /**
    *  服务器异常
    */
    SERVER_EXCEPTION(500,"未知的服务器异常"),

    DANGER_ERROR(500,"请求中有违反安全规则元素存在，拒绝访问!");

    /**
     * @Description 返回信息
     * @author wendy
     * @date 2017/12/8 14:14
     */
    RetEnum(int ret, String msg){
        setRet(ret);
        setMsg(msg);
    }

    /**
     * @Description 获取返回值
     * @author wendy
     * @date 2017/12/8 14:15
     * @param
     * @return   返回值
     */
    public int getRet() {
        return ret;
    }

    /**
     * @Description 设置返回值
     * @author wendy
     * @date 2017/12/8 14:15
     * @param   ret 返回值
     * @return
     */
    private void setRet(int ret) {
        this.ret = ret;
    }

    /**
     * @Description 获取返回信息
     * @author wendy
     * @date 2017/12/8 14:15
     * @param
     * @return   返回信息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @Description 设置返回信息
     * @author wendy
     * @date 2017/12/8 14:16
     * @param   msg 返回信息
     */
    private void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @Description 返回值
     * @author wendy 
     * @date 2017/12/8 14:16
     */  
    private int ret;
    /**
     * @Description 返回信息
     * @author wendy 
     * @date 2017/12/8 14:17
     */  
    private String msg;


}
