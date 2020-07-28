package com.unis.gameplatfrom.api;

import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.JsonSyntaxException;


import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.unis.gameplatfrom.event.BaseEvent;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 *
 * @author wulei
 * @date 2016/9/24
 */

@SuppressWarnings("DefaultFileTemplate")
public class SimpleDataSubscriber<T> implements Observer<T> {

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        if (!NetworkUtils.isConnected()) {
            onError(new NetworkException(-1, "当前网络不可用"));
            onComplete();
            return;
        }
    }

    @Override
    public void onNext(@NonNull T t) {
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            int code = httpException.code();
            if (code == 403) {
                //TOKEN失效
                BaseEvent.CommonEvent event = BaseEvent.CommonEvent.TOKEN_FAILURE;
                EventBus.getDefault().post(event);
            } else if (code >= 400 && code < 500) {
                exception(code, "数据请求异常，请稍候再试");
            } else if (code >= 500) {
                exception(code, "服务端异常，请稍候再试");
            } else {
                exception(code, "网络异常，请稍候再试");
            }
        } else if (e instanceof UnknownHostException) {
            exception(-2, "无法连接到服务器");
        } else if (e instanceof NetworkException) {
            exception(-2, "网络异常，请稍候再试");
        } else if (e instanceof SocketTimeoutException) {
            exception(-1, "连接超时, 请重试");
        } else if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            if (apiException.getCode() == 29014) {
                //用户被禁用
                BaseEvent.CommonEvent event = BaseEvent.CommonEvent.USER_ENABLE;
                EventBus.getDefault().post(event);
                exception(apiException.getCode(), "账户被禁用，请联系客服");
            } else if (apiException.getCode() == 10008) {
                //TOKEN失效
                BaseEvent.CommonEvent event = BaseEvent.CommonEvent.TOKEN_FAILURE;
                EventBus.getDefault().post(event);
                exception(apiException.getCode(), "登陆过期，重新登陆");
            }else {
                exception(apiException.getCode(), apiException.getMessage());
            }
        } else if (e instanceof JsonSyntaxException) {
            exception(-1, "数据解析异常");
        } else if (e instanceof ConnectException){
            exception(-1, "服务器维护中");
        }else {
            exception(-1, "未知异常");
        }
    }

    @Override
    public void onComplete() {

    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        if (!NetworkUtils.isAvailable(AppContext.getInstance())) {
//            onError(new NetworkException(-1, "当前网络不可用，请检查网络情况"));
//            onCompleted();
//            return;
//        }
//    }


    public void exception(int code, String message) {

    }

}
