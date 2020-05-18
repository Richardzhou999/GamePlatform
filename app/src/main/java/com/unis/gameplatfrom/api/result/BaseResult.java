package com.unis.gameplatfrom.api.result;

import java.io.Serializable;

/**
 * Created by wulei on 16/8/25.
 */

@SuppressWarnings("DefaultFileTemplate")
public class BaseResult implements Serializable {

    private int code;
    private String message;
    private String detail;

    public boolean isSuccess() {
        return code == 1;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
