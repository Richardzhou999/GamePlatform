package com.unis.kotlin.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.PermissionChecker
import android.util.Log
import java.util.ArrayList

class LinPermission {

    companion object {

        val RequestCode_Permission = 100
        private val TAG = "LinPermission"

        val requestPermissions = arrayOf(Manifest.permission.RECORD_AUDIO,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BODY_SENSORS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                //Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.SEND_SMS)

        /**
         * 检查是否有权限
         */
        fun checkPermission(context: Activity, code: Int): Boolean {
            try {
                return PackageManager.PERMISSION_GRANTED == PermissionChecker.checkSelfPermission(context, requestPermissions[code])
            } catch (e: Exception) {
                Log.e(TAG, "checkPermission: " + e.message)
            }

            return false
        }

        /**
         * 检查是否有所有权限
         */
        fun checkPermission(context: Activity, codes: IntArray): Boolean {
            for (code in codes) {
                if (!checkPermission(context, code)) {
                    return false
                }
            }
            return true
        }

        /**
         * 检查是否有所有权限
         */
        fun checkPermission(context: Activity, codes: Array<Int>): Boolean {
            for (code in codes) {
                if (!checkPermission(context, code)) {
                    return false
                }
            }
            return true
        }

        /**
         * 申请单一权限（指定开启某个权限）
         */
        fun requestPermission(context: Activity, code: Int) {
            if (!checkPermission(context, code)) {
                ActivityCompat.requestPermissions(context, arrayOf(requestPermissions[code]), RequestCode_Permission)
            }
        }

        /**
         * 申请多个权限
         */
        fun requestMultiplePermission(context: Activity, permissions: IntArray) {
            val unRequest = ArrayList<String>()
            for (code in permissions) {//遍历所有权限
                if (!checkPermission(context, code)) {//筛选未开启的权限
                    unRequest.add(requestPermissions[code])
                }
            }
            //判断list是否为空 不为空则进行申请
            if (unRequest.size > 0) {
                val permission = unRequest.toTypedArray()
                ActivityCompat.requestPermissions(context, permission, RequestCode_Permission)
            }
        }

        /**
         * 防止 禁止询问时 不会有提示窗  一般用于 申请失败时调用  否则 直接使用时跳转设置界面
         * @param context
         */
        fun requestPermission(context: Activity, permission: String) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) { //有权限申请提示
                ActivityCompat.requestPermissions(context, arrayOf(permission), RequestCode_Permission)
            } else {//点击禁止后不再提示动态申请  手动开启
                //可以自定义弹窗提示 是否手动开启
                //LinToPermission.init(context).jumpPermissionPage();
            }
        }

        /**
         * 申请结果回调
         * 在Activity的onRequestPermissionsResult中调用
         */
        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray, listener: PermissionsResultListener) {
            if (requestCode == RequestCode_Permission) {
                for (i in permissions.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        listener.onRequestPermissionSuccess(i, permissions[i])
                    } else {
                        listener.onRequestPermissionFailure(i, permissions[i])
                    }
                }
            }
        }
    }

    interface PermissionsResultListener {
        fun onRequestPermissionSuccess(pos: Int, permission: String)
        fun onRequestPermissionFailure(pos: Int, permission: String)
    }


}