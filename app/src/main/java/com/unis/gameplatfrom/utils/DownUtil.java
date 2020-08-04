package com.unis.gameplatfrom.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.unis.gameplatfrom.cache.NetConnectionReceiver;
import com.unis.gameplatfrom.cache.UserCenter;
import com.unis.gameplatfrom.entity.GamesEntity;
import com.unis.gameplatfrom.utils.download_mgr.DownloadTaskListener;
import com.unis.gameplatfrom.utils.udateapk.DownloadAPk;

import java.io.File;

public class DownUtil {

    private boolean mConnectTag;

    public boolean ismConnectTag() {
        return mConnectTag;
    }

    public void setmConnectTag(boolean mConnectTag) {
        this.mConnectTag = mConnectTag;
    }

    /**
     * 下载
     * @param filepath
     * @param entity
     * @param positoin
     * @param mDownloadMgr
     */
    private void downApk(Context context, String filepath, GamesEntity entity, int positoin,
                         DownloadMgr mDownloadMgr){


//        if(filepath.contains("apk")){
//
//            String[] apk_path = filepath.split("/");
//
//
//            String path = Environment.getExternalStorageDirectory()
//                    + "/DownLoad/apk/"+apk_path[apk_path.length-1];
//
//            if(!entity.isDownGame() ) {
//
//                if (PackageUtil.isAppByLocal(filepath)) {
//                    if(mDownloadMgr !=  null){
//                        mDownloadMgr.pauseAllTask();//暂停所有下载任务
//                        entity.setDownGame(false);
//                        UserCenter.getInstance().save_gameId(entity.getGameId());
//                        Intent installAppIntent = DownloadAPk.getInstallAppIntent(context, path);
//                        context.startActivity(installAppIntent);
//
//                    }
//
//                } else {
//
//                    if(NetworkUtils.isConnected()) {
//
//
//
//                        mConnectTag = true;
//                        progressList.add(positoin);
//                        //netConnectionReceiver = new NetConnectionReceiver(mDownloadMgr);
//                        //RegisterReceiver(netConnectionReceiver);
//                        //unregisterReceiver(itemNetConnectionReceiver);
//
//                        //显示activity时加入监听
//                        mDownloadTaskListener = new DownloadTaskListener() {
//                            @Override
//                            public void onStart(String taskId, long completeBytes, long totalBytes) {
//                                mDownloadTask = mDownloadMgr.getDownloadTask(taskId);
//
//                                saveDownName = entity.getName();
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        //netConnectionReceiver[0] = new NetConnectionReceiver(mDownloadMgr);
//                                        //RegisterReceiver(netConnectionReceiver[0]);
//                                    }
//                                });
//
//                            }
//
//                            @Override
//                            public void onProgress(String taskId, long currentBytes, long totalBytes) {
//                                mDownloadTask = mDownloadMgr.getDownloadTask(taskId);
//
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        for (int positoin : progressList) {
//                                            //ps:建议不要每次刷新 可以通过handler postDelay延时刷新 防止刷新频率过快
//                                            int progress = (int) (((float) currentBytes / totalBytes) * 100);
//
//                                            GamesEntity gamesEntity = adapter.getItem(positoin);
//                                            gamesEntity.setProgress(progress);
//                                            gamesEntity.setDownGame(true);
//                                            gamesEntity.setInstallGame(false);
//                                            gamesEntity.setLocal(false);
//                                            adapter.notifyItemChanged(positoin,gamesEntity);
//
//                                        }
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onPause(String taskId, long currentBytes, long totalBytes) {
//                                mDownloadTask = mDownloadMgr.getDownloadTask(taskId);
//                            }
//
//                            @Override
//                            public void onFinish(String taskId, File file) {
//                                mDownloadTask = mDownloadMgr.getDownloadTask(taskId);
//
//                                mDownloadMgr.removeListener(mDownloadTaskListener);
//                                if (progressList.size() != 0) {
//                                    progressList.clear();
//                                }
//
//                                mConnectTag = false;
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        // UnregisterReceiver(netConnectionReceiver);
//                                        // registerReceiver(itemNetConnectionReceiver, mFilter);
//
//                                        GamesEntity gamesEntity = adapter.getItem(positoin);
//                                        gamesEntity.setDownGame(false);
//                                        gamesEntity.setInstallGame(true);
//                                        gamesEntity.setLocal(true);
//                                        adapter.notifyItemChanged(positoin);
//                                        UserCenter.getInstance().save_gameId(entity.getGameId());
//                                        Intent installAppIntent = DownloadAPk.getInstallAppIntent(mContext, path);
//                                        startActivity(installAppIntent);
//
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onFailure(String taskId, String error_msg) {
//                                mDownloadTask = mDownloadMgr.getDownloadTask(taskId);
//                            }
//                        };
//
//                        mDownloadMgr.addListener(mDownloadTaskListener);
//
//                        DownloadMgr.Task task = new DownloadMgr.Task();
//                        task.setTaskId(mDownloadMgr.genTaskId() + entity.getName());       //生成一个taskId
//                        task.setUrl(entity.getP());   //下载地址
//                        task.setFilePath(path);    //下载后文件保存位置
//                        task.setDefaultStatus(DownloadMgr.DEFAULT_TASK_STATUS_START);       //任务添加后开始状态 如果不设置 默认任务添加后就自动开始
//
//                        mDownloadTask = mDownloadMgr.addTask(task);
//
//                    }else {
//
//                        ToastUtils.showShort("当前网络异常,无法下载");
//
//                    }
//
//                }
//
//            }
//
//
//        }else {
//
//            Toast.makeText(mContext,"游戏未上传,请跟客服人员联系",Toast.LENGTH_SHORT).show();
//            entity.setDownGame(false);
//
//        }



    }

    //态注册广播
    private void RegisterReceiver(NetConnectionReceiver mReceiver) {
//        if (!mReceiverTag){     //在注册广播接受者的时候 判断是否已被注册,避免重复多次注册广播
//            IntentFilter mFilter = new IntentFilter();
//            mReceiverTag = true;    //标识值 赋值为 true 表示广播已被注册
//            mFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//            registerReceiver(mReceiver, mFilter);
//        }
    }

    //注销广播
    private void UnregisterReceiver(NetConnectionReceiver mReceiver) {
//        if (mReceiverTag) {   //判断广播是否注册
//            mReceiverTag = false;   //Tag值 赋值为false 表示该广播已被注销
//            unregisterReceiver(mReceiver);   //注销广播
//        }

    }



}
