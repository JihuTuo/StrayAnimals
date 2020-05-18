package com.hsshy.beam.common.exception;
import com.hsshy.beam.common.constant.RetEnum;
import com.hsshy.beam.common.utils.support.StrKit;

public class BeamException extends RuntimeException {

    private Integer code;

    private String message;

    public BeamException(RetEnum retEnum) {
        this.code = retEnum.getRet();
        this.message = retEnum.getMsg();
    }

    public BeamException(String msg) {
        super(msg);
        this.message = msg;
    }

    public BeamException(String msg, Throwable e) {
        super(msg, e);
        this.message = msg;
    }

    public BeamException(String msg, int code, Throwable e) {
        super(msg, e);
        this.message = msg;
        this.code = code;
    }

    public BeamException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrKit.format(messageTemplate, params), throwable);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
