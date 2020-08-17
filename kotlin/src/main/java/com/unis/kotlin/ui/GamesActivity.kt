package com.unis.kotlin.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.unis.kotlin.R
import com.unis.kotlin.api.result.BaseCustomListResult
import com.unis.kotlin.base.BaseActivity
import com.unis.kotlin.constant.GameConstant
import com.unis.kotlin.entity.GamesEntity
import com.unis.kotlin.presenter.GamePresenter

class GamesActivity : BaseActivity<GamePresenter>(),GameConstant.View {

    private var gamePresenter : GamePresenter? = null

    private var gameList : ArrayList<GamesEntity>? = null


    override fun getLayout(): Int {
       return R.layout.activity_game
    }

    override fun initData() {

    }

    override fun initPresenter() {
        gamePresenter = GamePresenter(this)
        gamePresenter!!.attachView(this)
    }

    override fun initView(savedInstanceState: Bundle) {





    }

    override fun onSuccess(result: BaseCustomListResult<GamesEntity>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }









    override fun hideLoading() {}

    override fun onError(throwable: Throwable) {}

    override fun showLoading() {}



}
