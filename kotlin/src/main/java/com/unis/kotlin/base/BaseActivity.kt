package com.unis.kotlin.base

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.unis.kotlin.AppManager
import com.unis.kotlin.R
import com.unis.kotlin.presenter.BasePresenter
import com.unis.kotlin.ui.view.BaseView
import com.unis.kotlin.utils.StatusBarUtil

abstract class BaseActivity<P : BasePresenter<*,*>> : AppCompatActivity(),BaseView{

    private var mContext: Activity? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //4.4+透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (hasTransparencyStatusBar()) {
                StatusBarUtil.transparencyBar(this)
            } else {
                StatusBarUtil.setStatusBarColor(this, getStatusBarColor())
            }
            StatusBarUtil.StatusBarLightMode(this, 3)
        }

        mContext = this
        AppManager.get()!!.addActivity(this)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        if (getLayout() > 0) {
            setContentView(getLayout())
        }

        initPresenter()
        initView(savedInstanceState!!)
        initData()



    }



    @LayoutRes
    protected abstract fun getLayout(): Int

    protected abstract fun initData()

    /**
     * 初始化mPresenter
     */
    protected abstract fun initPresenter()

    protected abstract fun initView(savedInstanceState: Bundle)

    /**
     * 状态栏颜色
     *
     * @return
     */
    protected fun getStatusBarColor(): Int {
        return R.color.white
    }

    /**
     * 是否透明状态栏
     *
     * @return
     */
    protected fun hasTransparencyStatusBar(): Boolean {
        return true
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}