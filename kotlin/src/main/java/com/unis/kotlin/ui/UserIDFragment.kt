package com.unis.kotlin.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import com.blankj.utilcode.util.EmptyUtils
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.RegexUtils
import com.unis.kotlin.BaseApplication

import com.unis.kotlin.R
import com.unis.kotlin.base.BaseFragment
import com.unis.kotlin.cache.UserCenter
import com.unis.kotlin.constant.LoginConstant
import com.unis.kotlin.entity.LoginEntity
import com.unis.kotlin.presenter.LoginPresenter
import kotlinx.android.synthetic.main.fragment_user_id.*


class UserIDFragment : BaseFragment<LoginPresenter>(),LoginConstant.View ,View.OnClickListener{


    private var saveAccount : Boolean? = false

    private var loginPresenter : LoginPresenter? = null



    override fun initPresenter() {
        loginPresenter = LoginPresenter(mContext!!)
        loginPresenter!!.attachView(this)
    }


    override fun getLayout(): Int {
        return  R.layout.fragment_user_id
    }

    override fun initView(var1: View, var2: Bundle?) {

        val userID = UserCenter.get()!!.getUserid()


        if (!TextUtils.isEmpty(userID)) {

            txt_account.setText(userID)
            txt_account.setSelection(userID.length)
            save_account.setChecked(true)
            saveAccount = true

        } else {

            // mAccount.setText("");
            save_account.setChecked(false)
            saveAccount = false

        }


        txt_account.setSelection(txt_account.getText().toString().length)
        txt_password.setSelection(txt_password.getText().toString().length)





    }

    override fun initData() {
        click_layout.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                click_layout.setBackgroundResource(R.drawable.border_color)
            }else{
                click_layout.setBackgroundResource(android.R.color.transparent)
            }

        }

        login_password.setOnFocusChangeListener { view, hasFocus ->

            if (hasFocus){
                login_password.setBackgroundResource(R.drawable.border_color)
            }else{
                login_password.setBackgroundResource(android.R.color.transparent)
            }


        }



        save_account.setOnCheckedChangeListener { compoundButton, isChecked ->

            if(isChecked){
                saveAccount = true
            }else{
                saveAccount = false
                UserCenter.get()!!.delectUserid()

            }
        }

        btn_login_userId.setOnClickListener(this)
        click_layout.setOnClickListener(this)
        login_password.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){

            R.id.btn_login_userId ->{
                login()
            }
            R.id.login_password ->{

                val loginActivity = activity as LoginActivity?
                loginActivity!!.changeFragment(1)

            }

            R.id.click_layout ->{

                if (!saveAccount!!) {

                    saveAccount = true
                    save_account.setChecked(true)
                    save_text.setTextColor(ContextCompat.getColor(mContext!!, R.color.text_blue))



                } else {

                    saveAccount = false

                    save_account.setChecked(false)
                    save_text.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))
                    UserCenter.get()!!.delectUserid()

                }

            }


        }
    }

    fun login(){

        val mobile = txt_account.getText().toString().trim()
        val password = txt_password.getText().toString().trim({ it <= ' ' })

        if (EmptyUtils.isEmpty(mobile)) {
            showMessageDialog("请输入用户ID")
            return
        }

        if (EmptyUtils.isEmpty(password)) {
            showMessageDialog("请输入密码")
            return
        }

        if (NetworkUtils.isConnected()) {

            loginPresenter!!.login(mobile, password, "123", 2,
                    EncryptUtils.encryptMD5ToString(mobile + password + "UNIS").toLowerCase())

        } else {

            Toast.makeText(BaseApplication.getContext(), "网络异常,请检查网络", Toast.LENGTH_SHORT).show()

        }


    }


    override fun onSuccess(result: LoginEntity) {
        if (saveAccount!!) {
            UserCenter.get()!!.setUserid(txt_account.getText().toString().trim({ it <= ' ' }))
        }

        val intent = Intent(mContext, MainActivity::class.java)
        intent.putExtra("username", result.name)
        intent.putExtra("userhead", result.head)
        startActivity(intent)
        mContext!!.finish()
    }


}
