package com.unis.kotlin.api

import android.database.Observable
import com.blankj.utilcode.util.NetworkUtils
import com.google.gson.JsonSyntaxException
import com.unis.kotlin.BaseEvent
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

open class SimpleDataSubscriber<T> : Observer<T>{

    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {
        if (!NetworkUtils.isConnected()) {
            onError(NetworkException(-1, "当前网络不可用"))
            onComplete()
            return
        }
    }

    override fun onNext(t: T) {

    }

    override fun onError(e: Throwable) {

        if (e is HttpException) {
            val code = e.code()
            if (code == 403) {
                //TOKEN失效
                val event = BaseEvent.CommonEvent.TOKEN_FAILURE
                EventBus.getDefault().post(event)
            } else if (code >= 400 && code < 500) {
                exception(code, "数据请求异常，请稍候再试")
            } else if (code >= 500) {
                exception(code, "服务端异常，请稍候再试")
            } else {
                exception(code, "网络异常，请稍候再试")
            }
        } else if (e is UnknownHostException) {
            exception(-2, "无法连接到服务器")
        } else if (e is NetworkException) {
            exception(-2, "网络异常，请稍候再试")
        } else if (e is SocketTimeoutException) {
            exception(-1, "连接超时, 请重试")
        } else if (e is ApiException) {
            val apiException = e as ApiException
            if (apiException.getCode() == 29014) {
                //用户被禁用
                val event = BaseEvent.CommonEvent.USER_ENABLE
                EventBus.getDefault().post(event)
                exception(apiException.getCode(), "账户被禁用，请联系客服")
            } else if (apiException.getCode() == 10008) {
                //TOKEN失效
                val event = BaseEvent.CommonEvent.TOKEN_FAILURE
                EventBus.getDefault().post(event)
                exception(apiException.getCode(), "登陆过期，重新登陆")
            } else {
                exception(apiException.getCode(), apiException.message!!)
            }
        } else if (e is JsonSyntaxException) {
            exception(-1, "数据解析异常")
        } else if (e is ConnectException) {
            exception(-1, "服务器维护中")
        } else {
            exception(-1, "未知异常")
        }
    }


    open fun exception(code: Int, message: String) {}
}