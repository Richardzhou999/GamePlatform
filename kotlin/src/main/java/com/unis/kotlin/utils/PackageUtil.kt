package com.unis.kotlin.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Environment
import android.text.TextUtils
import android.widget.Toast
import com.unis.kotlin.cache.UserCenter
import java.io.File

class PackageUtil {

    companion object {


        /**
         * 检查应用是否存在，若存在便打开
         */

        fun startAppByPackageID(context: Context, packageId: String,
                                gameid: Int): Boolean {
            val packageManager = context.packageManager
            val packageInfo: PackageInfo
            try {
                // 应用包名
                packageInfo = packageManager.getPackageInfo(packageId, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(context, "没有找到应用", Toast.LENGTH_SHORT).show()
                return false
            }

            val resolveIntent = Intent(Intent.ACTION_MAIN, null)
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            resolveIntent.setPackage(packageInfo.packageName)
            val apps = packageManager.queryIntentActivities(resolveIntent, 0)
            val ri = apps.iterator().next()

            if (ri != null) {

                UserCenter.get()!!.save_gameId(gameid!!)
                val className = ri.activityInfo.name
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                val cn = ComponentName(packageInfo.packageName, className)
                intent.component = cn
                context.startActivity(intent)
            }
            return true
        }


        /**
         * 检查应用是否存在
         * @param packageName
         */
        fun isAppByPackageID(context: Context, packageName: String?): Boolean {
            if (packageName == null || "" == packageName)
                return false

            try {

                val info = context.packageManager.getApplicationInfo(
                        packageName, PackageManager.GET_UNINSTALLED_PACKAGES)
                return true

            } catch (e: PackageManager.NameNotFoundException) {
                return false
            }

        }


        /**
         * 检查应用是否已下载到本地
         * @param packageFile
         */
        fun isAppByLocal(packageFile: String): Boolean {

            if (!packageFile.contains("apk")) {
                return false
            }

            val apk_path = packageFile.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()


            val path = (Environment.getExternalStorageDirectory().toString()
                    + "/DownLoad/apk/" + apk_path[apk_path.size - 1])


            if (TextUtils.isEmpty(path)) {
                return false
            }

            val apkFile = File(path)
            // 如果文件夹不存在则创建
            return if (!apkFile.exists()) {
                false
            } else true

        }


        /**
         *
         * 隐藏应用图标
         * @param packageId
         */
        fun hideAppIcon(context: Context, packageId: String) {

            //        PackageManager packageManager = getPackageManager();
            //        PackageInfo packageInfo = null;
            //        String className = null;
            //
            //        try {
            //            // 应用包名
            //            packageInfo = packageManager.getPackageInfo( packageId, 0);
            //        } catch (PackageManager.NameNotFoundException e) {
            //            Toast.makeText(GamesActivity.this, "没有找到 应用", Toast.LENGTH_SHORT).show();
            //        }
            //        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            //        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            //        resolveIntent.setPackage(packageInfo.packageName);
            //        List<ResolveInfo> apps = packageManager.queryIntentActivities(resolveIntent, 0);
            //        ResolveInfo ri = apps.iterator().next();
            //        if (ri != null) {
            //            className = ri.activityInfo.name;
            //        }
            //        ComponentName componentName = new ComponentName(packageInfo.packageName, className);
            //        int res = packageManager.getComponentEnabledSetting(componentName);
            //        if (res == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
            //                || res == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            //            // 隐藏应用图标
            //            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            //                    PackageManager.DONT_KILL_APP);
            //        } else {
            //            // 显示应用图标
            //            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
            //                    PackageManager.DONT_KILL_APP);
            //        }


            val pm = context.packageManager
            if (PackageUtil.isAppByPackageID(context, packageId)) {
                if (isHideApp()) {
                    if (PackageManager.COMPONENT_ENABLED_STATE_DISABLED != pm.getApplicationEnabledSetting(packageId)) {

                        pm.setApplicationEnabledSetting(packageId, PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER, 0)
                        Toast.makeText(context, "隐藏成功", Toast.LENGTH_SHORT)
                    }
                } else {
                    if (PackageManager.COMPONENT_ENABLED_STATE_DISABLED == pm.getApplicationEnabledSetting(packageId)) {

                        pm.setApplicationEnabledSetting(packageId, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, 0)
                        Toast.makeText(context, " 显示成功", Toast.LENGTH_SHORT)
                    }
                }
            }
        }


        /**需要隐藏app的条件判断方法 */
        fun isHideApp(): Boolean {
            //条件
            return true
        }


        //获取当前应用的版本号：

        fun getVersionName(context: Context): String {
            // 获取packagemanager的实例
            val packageManager = context.packageManager
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            var packInfo: PackageInfo? = null
            try {
                packInfo = packageManager.getPackageInfo(context.packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return packInfo!!.versionName
        }


    }

}