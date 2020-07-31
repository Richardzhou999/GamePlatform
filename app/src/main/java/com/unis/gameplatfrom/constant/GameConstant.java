package com.unis.gameplatfrom.constant;

import com.unis.gameplatfrom.api.result.BaseCustomListResult;
import com.unis.gameplatfrom.entity.GamesEntity;
import com.unis.gameplatfrom.entity.LoginEntity;
import com.unis.gameplatfrom.model.BaseModel;
import com.unis.gameplatfrom.ui.view.BaseView;

import io.reactivex.Observable;

public interface GameConstant {

    interface Model extends BaseModel {
        Observable<BaseCustomListResult<GamesEntity>> getGameList(String uuid);
    }

    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable);

        void onSuccess(BaseCustomListResult<GamesEntity> result);
    }

    interface Presenter {
        /**
         * 登陆
         *
         * @param uuid
         */
        void getGameList(String uuid);
    }




}
