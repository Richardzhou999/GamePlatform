package com.unis.gameplatfrom.cache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


import com.unis.gameplatfrom.utils.DownloadMgr;

public class NetConnectionReceiver extends BroadcastReceiver {

    private DownloadMgr mDownloadMgr;

    public NetConnectionReceiver(DownloadMgr mDownloadMgr) {
        this.mDownloadMgr = mDownloadMgr;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            Log.e("xxx","xx"+networkInfos);
        }


        if(mobNetInfo != null && wifiNetInfo != null){

            if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                //网络连接已断开
//            for (DownloadTask downloadTask : downloadTasks) {
//                downloadTask.pauseDownload();//暂停所有下载任务
//            }
                Toast.makeText(context, "网络异常,请检查网络01", Toast.LENGTH_SHORT).show();
                if(mDownloadMgr != null ){

                    mDownloadMgr.pauseAllTask();//暂停所有下载任务
                }

            } else {
                //网络连接已连接
//            for (DownloadTask downloadTask : downloadTasks) {
//                downloadTask.startDownload();//继续所有下载任务
//            }

                if(mDownloadMgr != null ){
                    mDownloadMgr.startAllTask();//继续所有下载任务
                }
            }

        }

    }
}
