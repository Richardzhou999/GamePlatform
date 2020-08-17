package com.unis.kotlin.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.trello.rxlifecycle2.components.support.RxFragment
import com.unis.kotlin.presenter.BasePresenter
import com.unis.kotlin.ui.view.BaseView

abstract class BaseFragment<P : BasePresenter<*,*>> : RxFragment(),BaseView{

    protected var mContext : Activity? = null

    protected var mPresenter: P? = null


    @LayoutRes
    protected abstract fun getLayout(): Int

    protected abstract fun initView(var1: View, var2: Bundle?)

    protected abstract fun initData()

    /**
     * 初始化mPresenter
     */
    protected abstract fun initPresenter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = activity
        return inflater.inflate(this.getLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //初始化mPresenter
        initPresenter()
        //绑定view
        if (mPresenter != null) {
            //mPresenter!!.attachView(this)
        }


        initView(view,savedInstanceState)
        initData()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }


    protected fun showMessageDialog(msg: String) {
        val toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
        toast.show()
    }

}