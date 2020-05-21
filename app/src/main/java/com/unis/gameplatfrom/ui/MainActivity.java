package com.unis.gameplatfrom.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.adapter.MainAdapter;
import com.unis.gameplatfrom.base.BaseActivity;
import com.unis.gameplatfrom.utils.udateapk.LinPermission;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private String account;
    private String password;

    @BindView(R.id.rv_main)
    RecyclerView mainRecyclerView;

    private MainAdapter mainAdapter;


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
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,4);
        mainRecyclerView.setLayoutManager(gridLayoutManager);
        //mainAdapter = new MainAdapter(MainActivity.this,)


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
