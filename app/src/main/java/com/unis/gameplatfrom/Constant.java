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
    public static String DEBUG = "1.7.03-20200728-beta";

    public static final boolean IS_PRINT_LOG = true;//是否打印log,true为打印,false为不打印

}
