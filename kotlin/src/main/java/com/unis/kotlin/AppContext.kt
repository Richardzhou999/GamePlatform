package com.unis.kotlin

import com.blankj.utilcode.util.Utils
import com.unis.kotlin.BaseApplication
import com.unis.kotlin.MyCrashHandler
import org.litepal.LitePal

class AppContext : BaseApplication(){

    override fun onCreate() {
        super.onCreate()
        //初始化工具类
        Utils.init(this)
        //sqlite组件的初始化
        LitePal.initialize(this)


        //崩溃日志
        val handler = MyCrashHandler()
        Thread.setDefaultUncaughtExceptionHandler(handler)

    }
}