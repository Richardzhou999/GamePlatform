package com.unis.gameplatfrom;



import com.blankj.utilcode.util.Utils;

import org.litepal.LitePal;


/**
 * @author wulei
 * @date 16/8/25
 */

@SuppressWarnings("DefaultFileTemplate")
public class AppContext extends BaseApplication {

    private boolean isShowingEnable = false;
    private boolean isShowingTokenFailure = false;

    @Override
    public void onCreate() {
        super.onCreate();

//        EventBus.getDefault().register(this);
//
//        //初始化工具类
          Utils.init(this);
          //sqlite组件的初始化
          LitePal.initialize(this);



         initUpdata();




//        //初始化用户数据
//        UserCenter.getInstance();
//
//        Traceur.enableLogging();
//
//        initActivityRouter();
//
//        if (Constant.DEBUG) {
//            Stetho.initializeWithDefaults(this);
//        }
//
//        JPushInterface.setDebugMode(Constant.DEBUG);
//        JPushInterface.init(this);
//
//        GlideManager.setCommonPlaceholder(R.drawable.default_thumb);
//        GlideManager.setRoundPlaceholder(R.drawable.default_thumb);
//
//        initUmeng();
//        initQbSdk();
//        aliSDK();
//
//        CrashReport.initCrashReport(getApplicationContext(), Constant.BUGLY_APP_ID, Constant.DEBUG);

    }

    private void initUpdata() {



    }


//    private void initUmeng() {
//
//        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, Constant.UMENG_KEY, Constant.UMENG_CHANNEL_ID);
//        MobclickAgent.startWithConfigure(config);
//        //日志加密
//        MobclickAgent.enableEncrypt(true);
//        MobclickAgent.setDebugMode(Constant.DEBUG);
//
//        PlatformConfig.setWeixin("wx9cea3ec48bd23ec0", "68f9753708c4bcb705244fa3be1668e3");
//        PlatformConfig.setQQZone("1104701577", "gJ6xwuKMNmUmn5tS");
//        PlatformConfig.setSinaWeibo("1677245083", "6ef55bebfe8782e8da6bc77d1300be90", "http://aeonfantasy.universal-space.cn");
//
//        UMShareConfig shareConfig = new UMShareConfig();
//        shareConfig.isNeedAuthOnGetUserInfo(false);
//        shareConfig.isOpenShareEditActivity(true);
//        shareConfig.setSinaAuthType(UMShareConfig.AUTH_TYPE_SSO);
//
//        UMShareAPI.get(this);
//
//    }

    /**
     * 初始化Activity路由
     */
//    private void initActivityRouter() {
//        // 必填，填写独特的scheme，避免和其它APP重复
//        Router.init("mollyfantasy");
//        // 可选，如果需要支持HTTP协议就需要填写
////        Router.setHttpHost("www.unis.com");
//        // 可选，手工注册Activity
//        Router.register(new MLHXRouter());
//        // 可选，针对自己的业务做调整
//        Router.setFilter(new MLHXRouterFilter());
//    }





//    /**
//     * 初始化阿里百川
//     */
//    private void aliSDK(){
//        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
//            @Override
//            public void onSuccess() {
//                Log.d("AlibcTradeSDK","初始化成功");
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//                Log.d("AlibcTradeSDK","初始化失败,错误码="+i+" / 错误消息="+s);
//
//            }
//        });
//    }
}
