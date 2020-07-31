package com.unis.gameplatfrom.constant;

import com.unis.gameplatfrom.entity.LoginEntity;
import com.unis.gameplatfrom.model.BaseModel;
import com.unis.gameplatfrom.ui.view.BaseView;

import io.reactivex.Observable;

public interface LoginConstant {

    interface Model extends BaseModel {
        Observable<LoginEntity> login(String account, String password,
                                                        String netaddress, int type, String key);
    }

    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable);

        void onSuccess(LoginEntity result);
    }

    interface Presenter {
        /**
         * 登陆
         *
         * @param account
         * @param password
         */
        void login(String account, String password,
                   String netaddress, int type, String key);
    }




}
