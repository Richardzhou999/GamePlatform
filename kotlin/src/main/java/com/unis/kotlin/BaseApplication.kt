package com.unis.kotlin

import android.app.Application
import android.content.Context
import com.blankj.utilcode.util.Utils
import org.litepal.LitePal

open class BaseApplication : Application(){

    companion object {
        private var instance: BaseApplication? = null

        fun getInstance(): BaseApplication?{
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        //初始化工具类
        Utils.init(this)
        //sqlite组件的初始化
        LitePal.initialize(this)


        //崩溃日志
        val handler = MyCrashHandler()
        Thread.setDefaultUncaughtExceptionHandler(handler)


    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

    }


}


