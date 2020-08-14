package com.unis.kotlin.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.unis.kotlin.R
import com.unis.kotlin.base.BaseFragment
import com.unis.kotlin.constant.LoginConstant
import com.unis.kotlin.entity.LoginEntity
import kotlinx.android.synthetic.main.fragment_password_login.*


class PasswordFragment : BaseFragment() ,LoginConstant.View,View.OnClickListener{


    override fun getLayout(): Int {
        return R.layout.fragment_password_login
    }

    override fun initView(var1: View, var2: Bundle?) {

        btn_login_password.setOnClickListener(this)
        login_id.setOnClickListener(this)



    }

    override fun initData() {



    }



    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_login_password ->{

            }
            R.id.login_id ->{

            }

        }


    }



    override fun onDetach() {
        super.onDetach()
    }

    override fun onSuccess(result: LoginEntity) {
        super.onSuccess(result)

    }





}
