package com.unis.kotlin.constant


import com.unis.kotlin.api.result.BaseCustomListResult
import com.unis.kotlin.entity.GamesEntity
import com.unis.kotlin.model.BaseModel
import com.unis.kotlin.ui.view.BaseView
import io.reactivex.Observable

interface GameConstant {

    interface Model : BaseModel{

        fun getGameList(uuid: String): Observable<BaseCustomListResult<GamesEntity>>

    }

    interface View : BaseView{

        override fun hideLoading() {

        }

        override fun showLoading() {

        }

        override fun onError(throwable: Throwable) {

        }

        fun onSuccess(result: BaseCustomListResult<GamesEntity>)


    }

    interface Persenter{

        /**
         * 获取游戏列表
         */
        fun getGameList(uuid: String)


    }





}