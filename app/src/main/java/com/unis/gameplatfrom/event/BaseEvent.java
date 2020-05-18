package com.unis.gameplatfrom.event;

/**
 *
 * @author wulei
 * @date 2017/7/18
 */

@SuppressWarnings("DefaultFileTemplate")
public interface BaseEvent {

    /**
     *
     * @param obj
     */
    void setObject(Object obj);

    /**
     *
     * @return
     */
    Object getObject();

    /**
     * 事件定义
     */
    enum CommonEvent implements BaseEvent {
        //登录
        LOGIN,
        //登出
        LOGOUT,
        //修改用户信息
        UPDATE_USER,
        //一网通支付成功
        CMB_PAY_SUCCESS,
        //添加会员卡
        VIP_CARD_ADD,
        //积分转出
        VIP_CARD_INTEGRAL_OUT,
        //彩票转出
        VIP_CARD_LOTTERY_OUT,
        //会员卡挂失
        VIP_CARD_LOST,
        //点击启动广告
        LAUNCH_AD_CLICK,
        //用户被禁用
        USER_ENABLE,
        //TOKEN失效
        TOKEN_FAILURE,
        //刷新等级
        REFRESH_MEMBER_LEVEL,
        //购买成功
        BUY_SUCCESS,
        //跳转到会员卡TAB
        CHANGE_TO_VIP_CARD,
        //跳转商城
        GO_STORE,
        //跳转首页
        GO_HOME;

        private Object obj;

        @Override
        public void setObject(Object obj) {
            this.obj = obj;
        }

        @Override
        public Object getObject() {
            return obj;
        }
    }
}
