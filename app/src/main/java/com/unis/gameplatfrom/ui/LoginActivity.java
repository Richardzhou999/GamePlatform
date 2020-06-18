package com.unis.gameplatfrom.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.UserManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.umeng.analytics.MobclickAgent;
import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.api.HUDLoadDataSubscriber;
import com.unis.gameplatfrom.api.HttpResultFunc;
import com.unis.gameplatfrom.api.PublicApiInterface;
import com.unis.gameplatfrom.api.RetrofitWrapper;
import com.unis.gameplatfrom.api.result.BaseObjectResult;
import com.unis.gameplatfrom.base.BaseActivity;
import com.unis.gameplatfrom.cache.UserCenter;
import com.unis.gameplatfrom.model.GamesEntity;
import com.unis.gameplatfrom.model.LoginResult;
import com.unis.gameplatfrom.utils.PackageUtil;
import com.unis.gameplatfrom.utils.udateapk.LinPermission;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {



    private PassWordFragment passWordFragment;
    private UserIDFragment userIDFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(LinPermission.checkPermission(LoginActivity.this, new int[]{7,8})){



        }else {

            //首先申请权限
            LinPermission.requestMultiplePermission(LoginActivity.this,new int[]{7,8});
        }

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {

        //判断用户切换时需要隐藏或者显示的应用
        isHindORShowApp();

    }

    @Override
    protected void initView(Bundle savedInstanceState) {



        passWordFragment = (PassWordFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_password_login);
        userIDFragment = (UserIDFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_sms_login);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.show(userIDFragment).hide(passWordFragment).commit();



    }


    public void changeFragment(int type) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (type == 1) {
            transaction.show(passWordFragment).hide(userIDFragment).commit();
        } else if (type == 2) {
            transaction.show(userIDFragment).hide(passWordFragment).commit();
        }
    }








    private void isHindORShowApp() {



//        RetrofitWrapper.getInstance().create(PublicApiInterface.class)
//                .passwordLogin(mobile,password,EncryptUtils.encryptMD5ToString(mobile+password+"UNIS").toLowerCase())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribe(new HUDLoadDataSubscriber<LoginResult<GamesEntity>>(mContext) {
//                    @Override
//                    public void onNext(LoginResult<GamesEntity> result) {
//
//
//
//
//                    }
//
//                });

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LinPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, new LinPermission.PermissionsResultListener() {
            @Override
            public void onRequestPermissionSuccess(int pos, String permission) {

            }

            @Override
            public void onRequestPermissionFailure(int pos, String permission) {
                finish();
            }
        });
    }








}
