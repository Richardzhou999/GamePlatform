package com.unis.gameplatfrom.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.trello.rxlifecycle2.android.FragmentEvent;
import com.unis.gameplatfrom.api.HUDLoadDataSubscriber;
import com.unis.gameplatfrom.api.result.BaseObjectResult;
import com.unis.gameplatfrom.cache.UserCenter;
import com.unis.gameplatfrom.constant.LoginConstant;
import com.unis.gameplatfrom.entity.LoginEntity;
import com.unis.gameplatfrom.model.LoginModel;
import com.unis.gameplatfrom.ui.MainActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter<LoginConstant.Model,LoginConstant.View> implements LoginConstant.Presenter{

    private Context context;

    public LoginPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void login(String account, String password, String netaddress, int type, String key) {

        if(isViewAttached()) {
            getModule().login(account, password, netaddress, type, key)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    //.compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new HUDLoadDataSubscriber<LoginEntity>(context) {
                        @Override
                        public void onNext(LoginEntity result) {

                            if (result.getErr() == 0) {

                                //保存用户数据
                                UserCenter.getInstance().setUserName(result.getName());
                                UserCenter.getInstance().setUserHead(result.getHead());
                                UserCenter.getInstance().setToken(result.getUuid());
                                getView().onSuccess(result);

                            }
                            if (result.getErr() == 1) {
                                Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
}

    @Override
    protected LoginConstant.Model createModule() {
        return  new LoginModel();
    }

    @Override
    public void start() {

    }
}
