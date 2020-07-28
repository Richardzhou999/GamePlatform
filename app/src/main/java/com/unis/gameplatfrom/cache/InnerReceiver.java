package com.unis.gameplatfrom.cache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.unis.gameplatfrom.ui.GamesActivity;
import com.unis.gameplatfrom.utils.DownloadMgr;

public class InnerReceiver extends BroadcastReceiver {

    final String SYSTEM_DIALOG_REASON_KEY = "reason";

    final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

    final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";



    private DownloadMgr mDownloadMgr;


    public InnerReceiver(DownloadMgr mDownloadMgr) {
        this.mDownloadMgr = mDownloadMgr;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (reason != null) {
                if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                    Log.e("xxxx","HOME键被监听");

                    if(mDownloadMgr != null ){
                        mDownloadMgr.pauseAllTask();//暂停所有下载任务
                    }


                } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                    //Toast.makeText(GamesActivity.this, "多任务键被监听", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}