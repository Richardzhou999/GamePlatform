package com.unis.gameplatfrom.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.utils.udateapk.LinPermission;

public class MainActivity extends AppCompatActivity {

    private String account;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        account = getIntent().getStringExtra("account");
        password = getIntent().getStringExtra("password");



           // finish();
            //首先申请权限
            LinPermission.requestMultiplePermission(MainActivity.this,new int[]{7,8});

        if (LinPermission.checkPermission(MainActivity.this, new int[]{7,8})) {


            Intent intent = new Intent(MainActivity.this,GamesActivity.class);
            intent.putExtra("account",account);
            intent.putExtra("password",password);

            startActivity(intent);
            finish();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LinPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, new LinPermission.PermissionsResultListener() {
            @Override
            public void onRequestPermissionSuccess(int pos, String permission) {


                Intent intent = new Intent(MainActivity.this,GamesActivity.class);
                intent.putExtra("account",account);
                intent.putExtra("password",password);

                startActivity(intent);
                finish();
            }

            @Override
            public void onRequestPermissionFailure(int pos, String permission) {
                finish();
            }
        });
    }
}
