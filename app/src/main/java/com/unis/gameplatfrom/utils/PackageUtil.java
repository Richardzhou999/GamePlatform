package com.unis.gameplatfrom.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import com.unis.gameplatfrom.ui.GamesActivity;

import java.util.List;

public class PackageUtil {


    /**
     *  检查应用是否存在，若存在便打开
     */

    public static boolean startAppByPackageID(Context context, String packageId) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            // 应用包名
            packageInfo = packageManager.getPackageInfo( packageId, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "没有找到 应用", Toast.LENGTH_SHORT).show();
            return false;
        }
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageInfo.packageName);
        List<ResolveInfo> apps = packageManager.queryIntentActivities(resolveIntent, 0);
        ResolveInfo ri = apps.iterator().next();
        if (ri != null) {
            String className = ri.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageInfo.packageName, className);
            intent.setComponent(cn);
            context.startActivity(intent);
        }
        return true;
    }


    /**
     * 检查应用是否存在
     * @param packageName
     */
    public static boolean isAppByPackageID(Context context,String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(
                    packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /**
     *
     * 隐藏应用图标
     * @param packageId
     */
    public void hideAppIcon(Context context,String packageId){

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



        final PackageManager pm = context.getPackageManager();
        if(PackageUtil.isAppByPackageID(context,packageId)){
            if(isHideApp()){
                if(PackageManager.COMPONENT_ENABLED_STATE_DISABLED != pm.getApplicationEnabledSetting(packageId)){

                    pm.setApplicationEnabledSetting(packageId,PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER,0);
                    Toast.makeText(context,"隐藏成功",Toast.LENGTH_SHORT);
                }
            }else{
                if(PackageManager.COMPONENT_ENABLED_STATE_DISABLED == pm.getApplicationEnabledSetting(packageId)){

                    pm.setApplicationEnabledSetting(packageId,PackageManager.COMPONENT_ENABLED_STATE_DEFAULT ,0);
                    Toast.makeText(context," 显示成功",Toast.LENGTH_SHORT);
                }
            }
        }
    }


    /**需要隐藏app的条件判断方法**/
    public boolean isHideApp() {
        //条件
        return true;
    }












}
