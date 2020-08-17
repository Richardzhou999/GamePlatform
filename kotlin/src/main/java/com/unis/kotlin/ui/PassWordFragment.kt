package com.unis.kotlin.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.EditText
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
import kotlinx.android.synthetic.main.fragment_password_login.*


class PassWordFragment : BaseFragment<LoginPresenter>() ,LoginConstant.View,View.OnClickListener{

    private var saveAccount : Boolean? = false

    private var loginPresenter : LoginPresenter? = null


    override fun getLayout(): Int {
        return R.layout.fragment_password_login
    }

    override fun initPresenter() {

    }



    override fun initView(var1: View, var2: Bundle?) {

        btn_login_password.setOnClickListener(this)
        login_id.setOnClickListener(this)

    }

    override fun initData() {




        txt_account.setSelection(txt_account.getText().toString().length)
        txt_password.setSelection(txt_account.getText().toString().length)



        click_layout.setOnFocusChangeListener(View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                click_layout.setBackgroundResource(R.drawable.border_color)
            } else {
                click_layout.setBackgroundResource(android.R.color.transparent)
            }
        })

        login_id.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                login_id.setBackgroundResource(R.drawable.border_color)
            } else {
                login_id.setBackgroundResource(android.R.color.transparent)
            }
        })


        save_account.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                saveAccount = true
            } else {
                saveAccount = false
                UserCenter.get()!!.delectUserid()
            }
        })


    }



    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_login_password ->{
                login()
            }
            R.id.login_id ->{

                val loginActivity = activity as LoginActivity?
                loginActivity!!.changeFragment(2)

            }

            R.id.click_layout ->{

                if (!saveAccount!!) {

                    saveAccount = true
                    save_account.setChecked(true)
                    save_text.setTextColor(ContextCompat.getColor(BaseApplication.getContext()!!, R.color.text_blue))



                } else {

                    saveAccount = false

                    save_account.setChecked(false)
                    save_text.setTextColor(ContextCompat.getColor(BaseApplication.getContext()!!, R.color.white))
                    UserCenter.get()!!.delectUserid()

                }

            }

        }


    }

    fun login(){

        val mobile = txt_account.getText().toString().trim()
        val password = txt_password.getText().toString().trim({ it <= ' ' })

        if (EmptyUtils.isEmpty(mobile)) {
            showMessageDialog("请输入手机号")
            return
        } else if (!RegexUtils.isMobileExact(mobile)) {
            showMessageDialog("请输入正确的手机号")
            return
        }

        if (EmptyUtils.isEmpty(password)) {
            showMessageDialog("请输入密码")
            return
        }

        if (NetworkUtils.isConnected()) {

            loginPresenter!!.login(mobile, password, "SerialNo", 1,
                    EncryptUtils.encryptMD5ToString(mobile + password + "UNIS").toLowerCase())

        } else {

            Toast.makeText(BaseApplication.getContext(), "网络异常,请检查网络", Toast.LENGTH_SHORT).show()


        }


    }


    override fun onDetach() {
        super.onDetach()
    }

    override fun onSuccess(result: LoginEntity) {
        super.onSuccess(result)

    }





}
