package com.unis.kotlin.api

import com.google.gson.GsonBuilder
import com.unis.kotlin.API_PATH
import com.unis.kotlin.BaseApplication
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class RetrofitWrapper {

    companion object {

        private var instance : RetrofitWrapper? = null

        get() {
            if (field == null){
                field = RetrofitWrapper()
            }
            return field
        }

        @Synchronized
        fun get(): RetrofitWrapper{
            return instance!!
        }

    }

    private var retrofit: Retrofit? = null
    private var httpClient: OkHttpClient? = null



    constructor(){
        val executor = Executors.newCachedThreadPool()

        val gson = GsonBuilder().create()


        //        Interceptor requestInterceptor = chain -> {
        //            Request request = chain.request()
        //                    .newBuilder()
        //                    .addHeader("clientType", "Android")//客户端类型
        //                    .addHeader("appType", "Mollyfantasy")//APP类型
        //                    .addHeader("osVersion", String.valueOf(DeviceUtils.getSDKVersion()))//操作系统版本
        //                    .addHeader("version", appInfo.getVersionName())//APP版本号
        //                    .addHeader("versionCode", String.valueOf(appInfo.getVersionCode()))//APP版本代码
        //                    .addHeader("clientModel", DeviceUtils.getModel())//设备型号
        //                    .addHeader("manufacturer", DeviceUtils.getManufacturer())//设备产商
        //                    .addHeader("uuid", UTDevice.getUtdid(AppContext.getInstance()))//设备唯一标识
        //                    .addHeader("Authorization", EmptyUtils.isNotEmpty(UserCenter.getInstance().getToken()) ? "Bearer " + UserCenter.getInstance().getToken() : "")//登录Token
        //                    .build();
        //            return chain.proceed(request);
        //        };

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        //网络数据缓存
        val cacheFile = File(BaseApplication.getInstance()!!.getCacheDir(), "OkHttpCache")
        val cache = Cache(cacheFile, (1024 * 1024 * 100).toLong()) //100Mb



        httpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) //连接超时
                .readTimeout(60, TimeUnit.SECONDS) //读取超时
                .writeTimeout(60, TimeUnit.SECONDS) //写入超时
                .retryOnConnectionFailure(true) //超时重连

                .addInterceptor(logging)
                .cache(cache)
                .build()

        retrofit = Retrofit.Builder()
                .client(httpClient)
                .baseUrl(API_PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .callbackExecutor(executor)
                .build()
    }




    fun <T> create(service: Class<T>): T {
        return retrofit!!.create(service)
    }


}