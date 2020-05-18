package com.unis.gameplatfrom.utils;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import com.unis.gameplatfrom.ui.GamesActivity;

import java.util.List;

public class PageUtil {


//    /**
//     *  检查应用是否存在，若存在便打开
//     */
//
//    private boolean startAppByPackageID(String packageId) {
//        PackageManager packageManager = getPackageManager();
//        PackageInfo packageInfo;
//        try {
//            // 应用包名
//            packageInfo = packageManager.getPackageInfo( packageId, 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            Toast.makeText(GamesActivity.this, "没有找到 应用", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
//        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        resolveIntent.setPackage(packageInfo.packageName);
//        List<ResolveInfo> apps = packageManager.queryIntentActivities(resolveIntent, 0);
//        ResolveInfo ri = apps.iterator().next();
//        if (ri != null) {
//            String className = ri.activityInfo.name;
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            ComponentName cn = new ComponentName(packageInfo.packageName, className);
//            intent.setComponent(cn);
//            startActivity(intent);
//        }
//        return true;
//    }
//
//
//    /**
//     * 检查应用是否存在
//     * @param packageName
//     */
//    public boolean isAppByPackageID(String packageName) {
//        if (packageName == null || "".equals(packageName))
//            return false;
//        try {
//            ApplicationInfo info = getPackageManager().getApplicationInfo(
//                    packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//            return false;
//        }
//    }


    /**
     *
     * @param filepath
     * @param iconUrl
     */











}
