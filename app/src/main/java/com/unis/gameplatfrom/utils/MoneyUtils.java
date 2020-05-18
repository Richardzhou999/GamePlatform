package com.unis.gameplatfrom.utils;

import java.math.BigDecimal;

/**
 * Created by wulei on 2017/8/20.
 */

public class MoneyUtils {
    /**
     * 分转元
     *
     * @param cents 分
     * @return
     */
    public static String cents2Yuan(int cents) {
        String yuan = String.format("%.2f", cents / 100f);
        String str = new BigDecimal(yuan).stripTrailingZeros().toPlainString();
        return str;
    }

    /**
     * 折扣转换
     *
     * @param cents
     * @return
     */
    public static String cents2Discount(int cents) {
        String discount = String.format("%.2f", cents / 10f);
        String str = new BigDecimal(discount).stripTrailingZeros().toPlainString();
        return str;
    }

    public static String stripTrailingZeros(double number) {
        String str = new BigDecimal(number).stripTrailingZeros().toPlainString();
        return str;
    }

    public static String stripTrailingZeros(String number) {
        String str = new BigDecimal(number).stripTrailingZeros().toPlainString();
        return str;
    }
}
