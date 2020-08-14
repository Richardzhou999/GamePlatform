package com.unis.kotlin.presenter

import com.unis.kotlin.model.BaseModel
import com.unis.kotlin.ui.view.BaseView
import java.lang.ref.WeakReference

abstract class BaesPresenter<M : BaseModel,V : BaseView>{

    private var mvpView : WeakReference<V>? = null

    private var mvpModel: M? = null

    /**
     * 绑定view，一般在初始化中调用该方法
     *
     * @param view view
     */
    fun attachView(view: V) {
        this.mvpView = WeakReference(view)
        if (mvpModel == null) {
            mvpModel = createModule()
        }
    }

    /**
     * 解除绑定view，一般在onDestroy中调用
     */

    fun detachView() {
        if (null != mvpView) {
            mvpView!!.clear()
            mvpView = null
        }
        this.mvpModel = null
    }

    /**
     * View是否绑定
     *
     * @return
     */
    fun isViewAttached(): Boolean {
        return null != mvpView && null != mvpView!!.get()
    }

    protected fun getModule(): M? {
        return mvpModel
    }

    protected fun getView(): V? {
        return if(isViewAttached()) mvpView!!.get() else null
    }


    /**
     * 通过该方法创建Module
     */
    protected abstract fun createModule(): M

    /**
     * 初始化方法
     */
    abstract fun start()

}