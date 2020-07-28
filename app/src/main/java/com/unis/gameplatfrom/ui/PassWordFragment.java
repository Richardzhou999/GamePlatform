package com.unis.gameplatfrom.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.api.HUDLoadDataSubscriber;
import com.unis.gameplatfrom.api.PublicApiInterface;
import com.unis.gameplatfrom.api.RetrofitWrapper;
import com.unis.gameplatfrom.base.BaseFragment;
import com.unis.gameplatfrom.cache.UserCenter;
import com.unis.gameplatfrom.model.LoginResult;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 账号密码登陆
 */
public class PassWordFragment extends BaseFragment {


    @BindView(R.id.txt_account)
    EditText mAccount;

    @BindView(R.id.txt_password)
    EditText mPassword;

    @BindView(R.id.btn_login_password)
    Button mLoginBtn;

    @BindView(R.id.save_account)
    CheckBox mSaveBtn;

    @BindView(R.id.save_text)
    TextView mSaveText;

    @BindView(R.id.click_layout)
    LinearLayout mClickLayout;

    private boolean saveaccount;

    @BindView(R.id.login_id)
    TextView mLoginID;


    @Override
    protected int getLayout() {
        return R.layout.fragment_pass_word;
    }

    @Override
    protected void initView(View var1, Bundle var2) {


        String account = UserCenter.getInstance().getAccount();


        if(!TextUtils.isEmpty(account)){

            mAccount.setText(account);
            mAccount.setSelection(account.length());
            mSaveBtn.setChecked(true);

        }




//        mPassword.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if(mAccount.getText().toString().length() != 0){
//
//                    if(charSequence.length() != 0){
//
//                        mLoginBtn.setEnabled(true);
//                        mLoginBtn.setBackgroundResource(R.drawable.button_round_bule_normal);
//
//                    }else {
//
//                        mLoginBtn.setEnabled(false);
//                        mLoginBtn.setBackgroundResource(R.drawable.button_round_bule_down);
//
//                    }
//
//                }else {
//
//                    mLoginBtn.setEnabled(false);
//                    mLoginBtn.setBackgroundResource(R.drawable.button_round_bule_down);
//
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        mAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mAccount.setSelected(true);
                }else {
                    mAccount.setSelected(false);
                }
            }
        });

        mAccount.setSelection(mAccount.getText().toString().length());
        mPassword.setSelection(mAccount.getText().toString().length());

//        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    mAccount.setSelected(true);
//                }else {
//                    mAccount.setSelected(false);
//                }
//            }
//        });

        mClickLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mClickLayout.setBackgroundResource(R.drawable.border_color);
                }else {
                    mClickLayout.setBackgroundResource(android.R.color.transparent);
                }
            }
        });

        mLoginID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mLoginID.setBackgroundResource(R.drawable.border_color);
                }else {
                    mLoginID.setBackgroundResource(android.R.color.transparent);
                }
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

    @Override
    protected void initData() {

    }


    @OnClick({R.id.btn_login_password,R.id.login_id,R.id.click_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login_password:
                login();
                break;
            case R.id.login_id:
                LoginActivity loginActivity = (LoginActivity) getActivity();
                loginActivity.changeFragment(2);
                break;
            case R.id.click_layout:

                    if(!saveaccount){

                        saveaccount = true;
                        mSaveBtn.setChecked(true);
                        mSaveText.setTextColor(ContextCompat.getColor(mContext,R.color.text_blue));


                    }else {

                        saveaccount = false;
                        mSaveBtn.setChecked(false);
                        mSaveText.setTextColor(ContextCompat.getColor(mContext,R.color.white));

                    }

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


        if(NetworkUtils.isConnected()) {


            RetrofitWrapper.getInstance().create(PublicApiInterface.class)
                    .passwordLogin(mobile, password, "SerialNo", 1, EncryptUtils.encryptMD5ToString(mobile + password + "UNIS").toLowerCase())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new HUDLoadDataSubscriber<LoginResult>(mContext) {
                        @Override
                        public void onNext(LoginResult result) {

                            if (result.getErr() == 0) {
//                            Intent intent = new Intent();
//                            intent.putExtra("account",mobile);
//                            intent.putExtra("password",password);

                                if (saveaccount) {

                                    UserCenter.getInstance().setAccount(mobile);

                                }


                                UserCenter.getInstance().save_uuid(mContext, result.getUuid());

                                UserCenter.getInstance().setUserName(result.getName());
                                UserCenter.getInstance().setUserHead(result.getHead());

                                Intent intent = new Intent(mContext, MainActivity.class);

                                intent.putExtra("username", result.getName());
                                intent.putExtra("userhead", result.getHead());
                                startActivity(intent);
                                UserCenter.getInstance().setToken(result.getUuid());

                                mContext.finish();
                            }
                            if (result.getErr() == 1) {

                                Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_SHORT).show();


                            }
                        }
                    });

        }else {

            Toast.makeText(mContext, "网络异常,请检查网络", Toast.LENGTH_SHORT).show();


        }
    }



}
