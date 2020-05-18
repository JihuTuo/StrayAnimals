package com.hsshy.beam.common.utils;
import com.hsshy.beam.common.constant.RetEnum;
import lombok.*;
import java.io.Serializable;
/**
 * @description: 封装返回结果类
 * @author: hs
 * @create: 2018-09-21 22:42:04
 **/
@ToString
@AllArgsConstructor
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String msg;

    @Getter
    @Setter
    private T data;

    @Getter
    @Setter
    private boolean error;

    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
        this.error = false;
    }

    public R(int code, String msg, boolean error) {
        this.code = code;
        this.msg = msg;
        this.data = null;
        this.error = error;
    }

    public R(int code, String msg, boolean error, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.error = error;
    }

    public static R fail() {
        return new R(RetEnum.ERROR.getRet(), RetEnum.ERROR.getMsg(), true);
    }

    public static R fail(String msg) {
        return fail(RetEnum.ERROR.getRet(), msg);
    }

    public static R fail(int code, String msg) {
        return new R(code, msg, true);
    }

    public static <T> R<T> fail(T data) {
        return new R(RetEnum.ERROR.getRet(), RetEnum.ERROR.getMsg(), true, data);
    }

    public static <T> R<T> fail(int code, String msg, T data) {
        return new R(code, msg, true, data);
    }

    public static R ok() {
        return new R(RetEnum.SUCCESS.getRet(), RetEnum.SUCCESS.getMsg());
    }

    public static R ok(String msg) {
        String data = msg;
        return new R(RetEnum.SUCCESS.getRet(), msg, false, data);
    }

    public static R ok(int code, String msg) {
        return new R(code, msg, false, null);
    }

    public static <T> R<T> ok(T data) {
        return new R(RetEnum.SUCCESS.getRet(), RetEnum.SUCCESS.getMsg(), false, data);
    }

    public static <T> R<T> ok(int code, String msg, T data) {
        return new R(code, msg, false, data);
    }

}
