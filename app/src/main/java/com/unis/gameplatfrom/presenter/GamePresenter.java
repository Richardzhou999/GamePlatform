package com.unis.gameplatfrom.presenter;

import android.content.Context;

import com.unis.gameplatfrom.api.HUDLoadDataSubscriber;
import com.unis.gameplatfrom.api.result.BaseCustomListResult;
import com.unis.gameplatfrom.cache.UserCenter;
import com.unis.gameplatfrom.constant.GameConstant;
import com.unis.gameplatfrom.entity.GamesEntity;
import com.unis.gameplatfrom.model.GameModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GamePresenter extends BasePresenter<GameConstant.Model,GameConstant.View> implements GameConstant.Presenter{

    private Context context;

    public GamePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void getGameList(String uuid) {

        if(isViewAttached()){

             getModule().getGameList(UserCenter.getInstance().getToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    //.compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new HUDLoadDataSubscriber<BaseCustomListResult<GamesEntity>>(context) {
                        @Override
                        public void onNext(BaseCustomListResult<GamesEntity> Result) {

                                getView().onSuccess(Result);

                        }
                    });


        }


    }

    @Override
    protected GameConstant.Model createModule() {
        return  new GameModel();
    }

    @Override
    public void start() {

    }


}
