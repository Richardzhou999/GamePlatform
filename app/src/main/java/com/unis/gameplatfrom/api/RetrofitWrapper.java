package com.unis.gameplatfrom.api;

import com.blankj.utilcode.util.AppUtils;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.unis.gameplatfrom.AppContext;
import com.unis.gameplatfrom.Constant;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wulei on 16/4/21.
 */
@SuppressWarnings("DefaultFileTemplate")
public class RetrofitWrapper {

    private static RetrofitWrapper instance;

    private Retrofit retrofit;
    private OkHttpClient httpClient;

    private AppUtils.AppInfo appInfo;

    private RetrofitWrapper() {
        Executor executor = Executors.newCachedThreadPool();

        Gson gson = new GsonBuilder().create();

        BaseInterceptor requestIntercepter = new BaseInterceptor();

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

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //网络数据缓存
        File cacheFile = new File(AppContext.getInstance().getCacheDir(), "OkHttpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

        appInfo = AppUtils.getAppInfo();

        httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) //连接超时
                .readTimeout(60, TimeUnit.SECONDS) //读取超时
                .writeTimeout(60, TimeUnit.SECONDS) //写入超时
                .retryOnConnectionFailure(true) //超时重连
                .addInterceptor(requestIntercepter)
                .addInterceptor(logging)
                .addNetworkInterceptor(new StethoInterceptor())
                .cache(cache)
                .build();

        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(Constant.API_PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .callbackExecutor(executor)
                .build();
    }



    public static synchronized RetrofitWrapper getInstance() {
        if (instance == null) {
            synchronized (RetrofitWrapper.class) {
                instance = new RetrofitWrapper();
            }
        }
        return instance;
    }



    public Retrofit getRetrofit() {
        return retrofit;
    }

    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }

}
