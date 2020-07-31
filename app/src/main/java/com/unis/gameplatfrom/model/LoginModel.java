package com.unis.gameplatfrom.model;

import android.content.Intent;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.unis.gameplatfrom.api.PublicApiInterface;
import com.unis.gameplatfrom.api.RetrofitWrapper;
import com.unis.gameplatfrom.api.result.BaseObjectResult;
import com.unis.gameplatfrom.constant.LoginConstant;
import com.unis.gameplatfrom.entity.LoginEntity;
import com.unis.gameplatfrom.ui.MainActivity;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Field;

public class LoginModel implements LoginConstant.Model {

    @Override
    public Observable<LoginEntity> login(String account, String password,
                                                           String netaddress, int type, String key) {
        return RetrofitWrapper.getInstance().create(PublicApiInterface.class)
                .passwordLogin(account,password,netaddress,type,key);
    }


}
