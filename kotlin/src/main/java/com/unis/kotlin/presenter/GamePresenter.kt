package com.unis.kotlin.presenter

import android.content.Context
import android.widget.Toast
import com.unis.kotlin.api.HUDLoadDataSubscriber
import com.unis.kotlin.api.result.BaseCustomListResult
import com.unis.kotlin.cache.UserCenter
import com.unis.kotlin.constant.GameConstant
import com.unis.kotlin.entity.GamesEntity
import com.unis.kotlin.entity.LoginEntity
import com.unis.kotlin.model.GameModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GamePresenter : BasePresenter<GameConstant.Model,GameConstant.View>,GameConstant.Persenter{

    private var context : Context? = null

    constructor(context: Context){
        this.context = context;
    }


    override fun getGameList(uuid: String) {

        if(isViewAttached()){

            getModule()!!.getGameList(uuid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : HUDLoadDataSubscriber<BaseCustomListResult<GamesEntity>>(this.context!!) {
                        override fun onNext(result: BaseCustomListResult<GamesEntity>) {

                            getView()!!.onSuccess(result)

                        }
                    });

        }




    }

    override fun createModule(): GameConstant.Model {
        return GameModel()
    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}