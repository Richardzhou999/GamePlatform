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
     * 测试版
     */
    public static String DEBUG = "1.6.4-020617-beta";

}
