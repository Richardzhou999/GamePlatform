package com.unis.gameplatfrom.api;

/**
 * Created by wulei on 16/5/27.
 */
@SuppressWarnings("DefaultFileTemplate")
public class NetworkException extends RuntimeException {
    private int code;

    public NetworkException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
