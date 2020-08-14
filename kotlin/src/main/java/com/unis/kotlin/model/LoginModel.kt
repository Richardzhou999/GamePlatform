package com.unis.kotlin.model

import com.unis.kotlin.api.PublicApiInterface
import com.unis.kotlin.api.RetrofitWrapper
import com.unis.kotlin.constant.LoginConstant
import com.unis.kotlin.entity.LoginEntity
import io.reactivex.Observable

class LoginModel : LoginConstant.Model{

    override fun login(account: String, password: String, netaddress: String,
                       type: Int, key: String): Observable<LoginEntity> {

        return RetrofitWrapper.get().create(PublicApiInterface::class.java)
                .passwordLogin(account, password, netaddress, type, key)

    }


}