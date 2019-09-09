package com.phoenix.base.network.response;

public class BaseResponseBean<T> {
    private int code;
    private String message;
    private T Data;

    public boolean isCodeInvalid() {
        return code != 200;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}
