package com.unis.kotlin.ui.view

interface BaseView {

    /**
     * 显示加载中
     */
    abstract fun showLoading()

    /**
     * 隐藏加载
     */
    abstract fun hideLoading()

    /**
     * 数据获取失败
     * @param throwable
     */
    abstract fun onError(throwable: Throwable)

}