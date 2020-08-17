package com.unis.kotlin.constant

import com.unis.kotlin.entity.LoginEntity
import com.unis.kotlin.model.BaseModel
import com.unis.kotlin.ui.view.BaseView
import io.reactivex.Observable

interface LoginConstant{

    interface Model : BaseModel{

        abstract fun login(account: String, password: String,
                           netaddress: String, type: Int, key: String): Observable<LoginEntity>

    }

    interface View : BaseView{

        override fun hideLoading() {

        }

        override fun showLoading() {

        }

        override fun onError(throwable: Throwable) {

        }

        fun onSuccess(result: LoginEntity){}

    }

    interface Presenter{

        /**
         * 登陆
         *
         * @param account
         * @param password
         */
         fun login(account: String, password: String,
                           netaddress: String, type: Int, key: String)

    }





}