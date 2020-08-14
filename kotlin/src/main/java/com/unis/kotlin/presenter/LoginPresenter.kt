package com.unis.kotlin.presenter

import android.content.Context
import android.widget.Toast
import com.unis.kotlin.api.HUDLoadDataSubscriber
import com.unis.kotlin.cache.UserCenter
import com.unis.kotlin.constant.LoginConstant
import com.unis.kotlin.entity.LoginEntity
import com.unis.kotlin.model.LoginModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginPresenter : BaesPresenter<LoginConstant.Model, LoginConstant.View>,LoginConstant.Presenter{

    private var context: Context? = null;

    constructor(context: Context){
        this.context = context;
    }

    override fun login(account: String, password: String, netaddress: String, type: Int, key: String) {

        if (isViewAttached()) {
            getModule()!!.login(account, password, netaddress, type, key)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    //.compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(object : HUDLoadDataSubscriber<LoginEntity>(this.context!!) {
                        override fun onNext(result: LoginEntity) {

                            if (result.err == 0) {

                                //保存用户数据
                                UserCenter.get()!!.setUserName(result.name)
                                UserCenter.get()!!.setUserHead(result.head)
                                UserCenter.get()!!.setToken(result.uuid)
                                getView()!!.onSuccess(result)

                            }
                            if (result.err == 1) {
                                Toast.makeText(context, result.msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    });
    }



    }


    override fun createModule(): LoginConstant.Model {
        return LoginModel()
    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}