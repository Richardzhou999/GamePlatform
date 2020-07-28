package com.unis.gameplatfrom.api.result;


/**
 * Created by wulei on 16/8/25.
 */
@SuppressWarnings("DefaultFileTemplate")
public class BaseObjectResult<T> extends BaseResult{

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
