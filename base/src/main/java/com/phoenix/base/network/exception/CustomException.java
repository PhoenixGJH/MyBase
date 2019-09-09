package com.phoenix.base.network.exception;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

public class CustomException {
    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;

    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;

    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 1002;

    /**
     * 协议错误
     */
    public static final int HTTP_ERROR = 1003;

    public static ApiException handleException(Throwable throwable) {
        ApiException exception;
        if (throwable instanceof JsonParseException ||
                throwable instanceof JSONException ||
                throwable instanceof ParseException) {
            //解析错误
            exception = new ApiException(PARSE_ERROR, throwable.getMessage());
        } else if (throwable instanceof ConnectException) {
            //网络错误
            exception = new ApiException(NETWORK_ERROR, throwable.getMessage());
        } else if (throwable instanceof UnknownHostException ||
                throwable instanceof SocketTimeoutException) {
            //连接错误
            exception = new ApiException(HTTP_ERROR, throwable.getMessage());
        } else if (throwable instanceof ApiException) {
            exception = ((ApiException) throwable);
        } else {
            //未知错误
            exception = new ApiException(UNKNOWN, throwable.getMessage());
        }
        return exception;
    }
}
