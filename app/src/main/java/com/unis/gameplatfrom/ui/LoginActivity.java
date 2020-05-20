package com.unis.gameplatfrom.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.UserManager;
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

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.txt_account)
    EditText mAccount;

    @BindView(R.id.txt_password)
    EditText mPassword;

    @BindView(R.id.btn_login)
    Button mLoginBtn;

    @BindView(R.id.save_account)
    CheckBox mSaveBtn;


    private boolean saveaccount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {


        String account = UserCenter.getInstance().getAccount();


        if(!TextUtils.isEmpty(account)){

            mAccount.setText(account);
            mSaveBtn.setChecked(true);

        }


        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if(mAccount.getText().toString().length() != 0){

                        if(charSequence.length() != 0){

                            mLoginBtn.setEnabled(true);
                            mLoginBtn.setBackgroundResource(R.drawable.button_round_bule_normal);

                        }else {

                            mLoginBtn.setEnabled(false);
                            mLoginBtn.setBackgroundResource(R.drawable.button_round_bule_down);

                        }


                    }else {

                        mLoginBtn.setEnabled(false);
                        mLoginBtn.setBackgroundResource(R.drawable.button_round_bule_down);


                    }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        mSaveBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    saveaccount = true;
                }else {
                    saveaccount = false;
                }
            }
        });
    }

    @OnClick({R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            default:
                break;
        }
    }

    private void login() {

        String mobile = mAccount.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (EmptyUtils.isEmpty(mobile)) {
            showMessageDialog("请输入手机号");
            return;
        }
//        else if (!RegexUtils.isMobileSimple(mobile)) {
//            showMessageDialog("请输入正确的手机号");
//            return;
//        }

        if (EmptyUtils.isEmpty(password)) {
            showMessageDialog("请输入密码");
            return;
        }

        RetrofitWrapper.getInstance().create(PublicApiInterface.class)
                .passwordLogin(mobile, password, EncryptUtils.encryptMD5ToString(mobile+password+"UNIS").toLowerCase())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new HUDLoadDataSubscriber<LoginResult<GamesEntity>>(mContext) {
                    @Override
                    public void onNext(LoginResult<GamesEntity> result) {

                        if(result.getErr() == 0){
//                            Intent intent = new Intent();
//                            intent.putExtra("account",mobile);
//                            intent.putExtra("password",password);

                            if(saveaccount){

                                UserCenter.getInstance().setAccount(mobile);

                            }

                            //判断用户切换时需要隐藏或者显示的应用
                            isHindORShowApp();


                            UserCenter.getInstance().setGameAccount(mobile);


                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("account",mobile);
                            intent.putExtra("password",password);
                            startActivity(intent);
                            UserCenter.getInstance().setToken(result.getUuid());


                        }
                        mContext.finish();
                    }
                });
    }



    private void isHindORShowApp() {

        String mobile = mAccount.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        RetrofitWrapper.getInstance().create(PublicApiInterface.class)
                .passwordLogin(mobile,password,EncryptUtils.encryptMD5ToString(mobile+password+"UNIS").toLowerCase())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new HUDLoadDataSubscriber<LoginResult<GamesEntity>>(mContext) {
                    @Override
                    public void onNext(LoginResult<GamesEntity> result) {




                    }

                });

    }









}
