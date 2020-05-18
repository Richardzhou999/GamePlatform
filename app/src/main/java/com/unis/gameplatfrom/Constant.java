package com.unis.gameplatfrom;


import com.unis.gameplatfrom.cache.UserCenter;

/**
 * @author wulei
 * @date 2016/10/19
 */

@SuppressWarnings("DefaultFileTemplate")
public class Constant {

    /**
     * 接口域名
     */
    public static  String HOST = UserCenter.getInstance().getHost();


    public static  String API_PATH = HOST ;

    /**
     * 娃娃机host
     */
    public static final String WAWA_HOST = "http://wwj.mollyfantasy.universal-space.cn/?token=";
//    public static final String WAWA_HOST = "http://10.1.60.244?token=";//测试主机娃娃机

    /**
     * 静态文件host
     */
    public static final String STATIC_PATH = "https://static.mollyfantasy.universal-space.cn/static";

    /**
     * 积分商城host
     */
    public static final String WEB_MALL_HOST = "http://gdmolihx.stage.dmgc.us/public/";

    /**
     * 测试时的状态，注意打包时修改为false
     */
    public static final boolean DEBUG = false;

    /**
     * 一页加载数据数目
     */
    public static final int PAGE_SIZE = 20;

    /**
     * 订单支付允许时间 14分钟(毫秒)
     */
    public static final int ORDER_PAY_ALLOW_TIME = 14 * 60 * 1000;

    /**
     * 招行一网通支付URL
     */
    public static final String CMB_PAY_URL = "https://netpay.cmbchina.com/netpayment/BaseHttp.dll?MB_EUserPay";//正式
//    public static final String CMB_PAY_URL = "http://121.15.180.66:801/netpayment/BaseHttp.dll?MB_EUserPay";//测试
    /**
     * 一网通支付成功回调地址
     */
    public static final String CMB_PAY_RETURN_URL = HOST + "/cmbpay_success";

    /**
     * 邀请好友URL
     */
    public static final String INVITE_FRIEND_URL = STATIC_PATH + "/mlhx/invite.html";

    /**
     * 资讯详情URL
     */
    public static final String INFORMATION_DETAIL_URL = STATIC_PATH + "/mlhx/information_detail.html?information_id=%s#%s";

    /**
     * 广告详情URL
     */
    public static final String AD_DETAIL_URL = STATIC_PATH + "/mlhx/ad_detail.html?ad_id=%s#t=%s";

    /**
     * 用户协议
     */
    public static final String USER_AGREEMENT_URL = STATIC_PATH + "/html/register_agreement.html";

    /**
     * 分享logo
     */
    public static final String LOGO_URL = STATIC_PATH + "/images/icon.jpg";

    /**
     * 短信验证码等待时间
     */
    public static final int SMS_DELAY_TIME = 60;

    /**
     * Umeng key
     */
    public static final String UMENG_KEY = "557a68ce67e58e15380016f8";
    public static final String UMENG_CHANNEL_ID = "Official";

    /**
     * bugly app id
     */
    public static final String BUGLY_APP_ID = "a397c8ff4f";
}
