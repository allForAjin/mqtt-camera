package com.lmk.mqtt.entity.result;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ResponseResult<T> implements Serializable {
    private Integer code;
    private String message;
    private final Map<String, T> data = new HashMap<>();

    public ResponseResult(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public ResponseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseResult(Integer code, String message, T data) {
        this(code,message);
        this.data.put("data", data);
    }

    public static <E> ResponseResult<E> success(ResultCode resultCode) {
        return new ResponseResult<E>(resultCode);
    }

    public static <E> ResponseResult<E> success() {
        return new ResponseResult<E>(ResultCode.SUCCESS);
    }

    public static <E> ResponseResult<E> success(String message) {
        return new ResponseResult<>(ResultCode.SUCCESS_CODE,message);
    }

    public static <E> ResponseResult<E> fail(ResultCode resultCode) {
        return new ResponseResult<E>(resultCode);
    }

    public static <E> ResponseResult<E> fail(String message) {
        return new ResponseResult<>(ResultCode.FAIL_CODE,message);
    }

    public static <E> ResponseResult<E> fail() {
        return new ResponseResult<E>(ResultCode.COMMON_FAIL);
    }

    public ResponseResult<T> addData(String key, T data) {
        this.data.put(key, data);
        return ResponseResult.this;
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
