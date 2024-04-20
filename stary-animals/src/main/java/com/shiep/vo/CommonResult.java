package com.shiep.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonResult {
    private int code;
    private Object data;
    private String message;

    public CommonResult(int code) {
        this.code = code;
    }

    public CommonResult(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public CommonResult(int code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
