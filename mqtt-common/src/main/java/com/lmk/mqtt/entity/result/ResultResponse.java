package com.lmk.mqtt.entity.result;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ResultResponse<T> implements Serializable {
    private Integer code;
    private String message;
    private final Map<String, T> data = new HashMap<>();

    public ResultResponse(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public static <E> ResultResponse<E> success(ResultCode resultCode) {
        return new ResultResponse<E>(resultCode);
    }

    public static <E> ResultResponse<E> success() {
        return new ResultResponse<E>(ResultCode.SUCCESS);
    }

    public static <E> ResultResponse<E> fail(ResultCode resultCode) {
        return new ResultResponse<E>(resultCode);
    }

    public static <E> ResultResponse<E> fail() {
        return new ResultResponse<E>(ResultCode.COMMON_FAIL);
    }

    public ResultResponse<T> addData(String message, T data) {
        this.data.put(message, data);
        return ResultResponse.this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, T> getData() {
        return data;
    }
}
