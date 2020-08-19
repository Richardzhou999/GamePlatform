package com.unis.kotlin.constant


import com.unis.kotlin.api.result.BaseCustomListResult
import com.unis.kotlin.entity.GamesEntity
import com.unis.kotlin.model.BaseModel
import com.unis.kotlin.ui.view.BaseView
import io.reactivex.Observable

interface MainConstant {

    interface Model : BaseModel{

        fun getGameList(uuid: String) : Observable<BaseCustomListResult<GamesEntity>>
    }

    interface View : BaseView{

        override fun hideLoading() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun showLoading() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onError(throwable: Throwable) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        fun onSuccess(result: BaseCustomListResult<GamesEntity>)

    }

    interface Presenter{

        fun getGameList(uuid: String)

    }

}