package com.unis.gameplatfrom.api;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BaseInterceptor implements Interceptor{

    @Override
    public Response intercept(Chain chain) throws IOException {

//        Request oldRequest = chain.request();
//        Request.Builder requestBuilder = oldRequest.newBuilder()
//                .addHeader("clientType", "Android")//客户端类型
//                .addHeader("appType", "Mollyfantasy")//APP类型
//                .addHeader("osVersion", String.valueOf(DeviceUtils.getSDKVersion()))//操作系统版本
//                .addHeader("version", AppUtils.getAppInfo().getVersionName())//APP版本号
//                .addHeader("versionCode", String.valueOf(AppUtils.getAppInfo().getVersionCode()))//APP版本代码
//                .addHeader("clientModel", DeviceUtils.getModel())//设备型号
//                .addHeader("manufacturer", DeviceUtils.getManufacturer())//设备产商
//                .addHeader("uuid", UTDevice.getUtdid(AppContext.getInstance()))//设备唯一标识
//                .addHeader("Authorization", EmptyUtils.isNotEmpty(UserCenter.getInstance().getToken()) ? "Bearer " + UserCenter.getInstance().getToken() : "");//登录Token;
//
//        long nowtime = System.currentTimeMillis();
//        List<String> list1 = new ArrayList<>();
//        for (String key : oldRequest.url().queryParameterNames()){
//            list1.add(key);
//        }
//        list1.add("apptime");
//        Collections.sort(list1);
//        String appsign = "";
//        for(String key : list1){
//            if (key.equals("apptime")){
//                appsign += nowtime+"";
//            }else {
//                appsign += oldRequest.url().queryParameterValues(key).get(0);
//            }
//        }
//        appsign = EncryptUtils.encryptMD5ToString(appsign + "123").toLowerCase();
//        // 添加新的参数
//        HttpUrl.Builder commonParamsUrlBuilder = oldRequest.url()
//                .newBuilder()
//                .scheme(oldRequest.url().scheme())
//                .host(oldRequest.url().host())
//                .addQueryParameter("appkey", "12311")
//                .addQueryParameter("apptime", nowtime+"")
//                .addQueryParameter("appsign", appsign);
//
//        requestBuilder.url(commonParamsUrlBuilder.build());
//
//        Request request = requestBuilder.build();

        Request request = chain.request();
        return chain.proceed(request);
    }
}
