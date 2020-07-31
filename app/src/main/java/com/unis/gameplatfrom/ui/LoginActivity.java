package com.unis.gameplatfrom.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.api.HUDLoadDataSubscriber;
import com.unis.gameplatfrom.api.HttpResultFunc;
import com.unis.gameplatfrom.api.RetrofitWrapper;
import com.unis.gameplatfrom.api.result.BaseObjectResult;
import com.unis.gameplatfrom.base.BaseActivity;
import com.unis.gameplatfrom.cache.UserCenter;
import com.unis.gameplatfrom.utils.PackageUtil;
import com.unis.gameplatfrom.utils.udateapk.LinPermission;

public class LoginActivity extends AppCompatActivity{



    private PassWordFragment passWordFragment;
    private UserIDFragment userIDFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(LinPermission.checkPermission(LoginActivity.this, new int[]{7,8})){



        }else {

            //首先申请权限
            LinPermission.requestMultiplePermission(LoginActivity.this,new int[]{7,8});
        }

        passWordFragment = (PassWordFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_password_login);
        userIDFragment = (UserIDFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_sms_login);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.show(userIDFragment).hide(passWordFragment).commit();


        //每次登陆前先清缓存
        UserCenter.getInstance().deleteDownFile();

    }

    public void changeFragment(int type) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (type == 1) {
            transaction.show(passWordFragment).hide(userIDFragment).commit();
        } else if (type == 2) {
            transaction.show(userIDFragment).hide(passWordFragment).commit();
        }
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
