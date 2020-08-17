package com.unis.kotlin

import android.app.Activity
import android.content.Context
import android.util.Log
import java.util.*

class AppManager {

    private var activityStack: Stack<Activity>? = null

    companion object {

        private val TAG = "AppManager"


        private var instance: AppManager? = null

        get() {

            if(field == null){
                field = AppManager()
            }

            return field
        }

        /**
         * 单一实例
         */
        @Synchronized
        fun get():AppManager?{
            return instance
        }


    }


    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
        Log.d(TAG, "addActivity: " + activityStack.toString())
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity {
        return activityStack!!.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        if (activityStack != null && !activityStack!!.empty()) {
            val activity = activityStack!!.lastElement()
            finishActivity(activity)
        }
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            activityStack!!.remove(activity)
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        Log.d(TAG, "finishActivity: " + activityStack.toString())

    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack!!) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
                break
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (null != activityStack!!.get(i)) {
                finishActivity(activityStack!!.get(i))
            }
            i++
        }
        activityStack!!.clear()
    }

    /**
     * 获取指定的Activity
     *
     * @author kymjs
     */
    fun getActivity(cls: Class<*>): Activity? {
        if (activityStack != null) {
            for (activity in activityStack!!) {
                if (activity.javaClass == cls) {
                    return activity
                }
            }
        }
        return null
    }

    fun hasTopActivity(activity: Activity): Boolean {
        return activityStack != null && activityStack!!.size > 0 && activityStack!!.get(0).javaClass == activity
    }

    /**
     * 获取堆栈全部类
     */
    fun getTask(): List<String> {
        val listActivity = ArrayList<String>()
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            val activity = activityStack!!.get(i)
            val activityname = activity.toString()
            listActivity.add(activityname)
            i++
        }
        return listActivity
    }

    /**
     * 退出应用程序
     */
    fun appExit(context: Context) {
        try {
            finishAllActivity()
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)
        } catch (e: Exception) {
        }

    }




}