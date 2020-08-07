package com.unis.gameplatfrom.utils.udateapk;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.blankj.utilcode.util.LogUtils;
import com.unis.gameplatfrom.utils.LogUtil;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;


public class DownloadAPk {

        private static int FILE_LEN = 0;
        private static RemoteViews mNotifiviews;
        public static String APK_UPGRADE = Environment
                .getExternalStorageDirectory() + "/DownLoad/apk";
        private static PendingIntent nullIntent;
        private static Context mContext;
        private static String mIconUrl;

        private static String TAG = "DownloadAPk";

        public interface GameProgressListener{

            public void getProgress(int progress);

            public void endDown();
        }

        private static GameProgressListener gameProgressListener;



    /**
         * 判断8.0 安装权限
         */
        public static void downApk(Context context, String url,String localAddress,String iconUrl,
                                   GameProgressListener progressListener) {
            mContext = context;
            mIconUrl = iconUrl;
            gameProgressListener = progressListener;
            if (Build.VERSION.SDK_INT >= 26) {
                boolean b = context.getPackageManager().canRequestPackageInstalls();
                if (b) {
                    if(TextUtils.isEmpty(localAddress)){
                        downloadAPK( url, null);
                    }else {
                        downloadAPK( url, localAddress);
                    }
                    downloadAPK( url, null);
                } else {
                    //请求安装未知应用来源的权限
                    startInstallPermissionSettingActivity();
                }
            } else {
                if(TextUtils.isEmpty(localAddress)){
                    downloadAPK( url, null);
                }else {
                    downloadAPK( url, localAddress);
                }
            }
        }

        /**
         * 开启安装APK权限(适配8.0)
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        public static void startInstallPermissionSettingActivity() {
            Uri packageURI = Uri.parse("package:" + mContext.getPackageName());
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
            mContext.startActivity(intent);
        }

        /**
         * 下载APK文件
         */
        private static void downloadAPK( String url,String localAddress)
        {
            // 下载
            if (localAddress != null)
            {
                APK_UPGRADE = localAddress;
            }

            downloadFile(url);
        }



    public static void downloadFile(String url) {

        final long startTime = System.currentTimeMillis();
        LogUtil.i(TAG,"startTime="+startTime);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Connection", "close")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                LogUtil.i(TAG,"download failed");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;

                try {

                    File apkFile = new File(APK_UPGRADE);
                    // 如果文件夹不存在则创建
                    if (!apkFile.getParentFile().exists())
                    {
                        apkFile.getParentFile().mkdirs();
                    }

                    is = response.body().byteStream();
                    long total = response.body().contentLength();

                    fos = new FileOutputStream(apkFile);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {

                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.e("download","下载进度"+progress);
                        //mView.onDownloading("",progress);
                        //upDataNotify(progress);
                        gameProgressListener.getProgress(progress);
                    }
                    fos.flush();
                    //finishNotify();
                    gameProgressListener.endDown();
                    LogUtil.e(TAG,"下载路径："+apkFile.getAbsolutePath());
                    LogUtil.e(TAG,"download success");
                    LogUtil.e(TAG,"totalTime="+ (System.currentTimeMillis() - startTime));
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e(TAG,"download failed : "+e.getMessage());
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }


    private static void upDataNotify(int progress)
    {
        LinNotify.showProgress(mContext,mIconUrl,0,
                "正在下载..."+progress+"%",null,
                null,PRIORITY_DEFAULT,null,LinNotify.TYPE_PROGRESS,
                LinNotify.NEW_MESSAGE,progress);
    }


    private static void finishNotify()
    {

        Intent installAppIntent = getInstallAppIntent(mContext,APK_UPGRADE);
        LinNotify.show(mContext,null,"下载完成",null,null,LinNotify.TYPE_PROGRESS,LinNotify.NEW_MESSAGE,null);
        //LinNotify.CannelNotificationChannel(mContext,0);
        mContext.startActivity(installAppIntent);

    }


    public static void InstanllGame(){

        Intent installAppIntent = getInstallAppIntent(mContext,APK_UPGRADE);
        mContext.startActivity(installAppIntent);


    }






    /**
     * 调往系统APK安装界面（适配7.0）
     * @return
     */
    public static Intent getInstallAppIntent(Context context,String filePath) {
        mContext  = context;
        //apk文件的本地路径
        File apkfile = new File(filePath);
        if (!apkfile.exists()) {
            return null;
        }
        Log.e("getInstallAppIntent","安装路径："+apkfile.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri contentUri = getUriForFile(apkfile);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");

        return intent;
    }

    /**
     * 将文件转换成uri
     * @return
     */
    public static Uri getUriForFile(File file) {
        LogUtils.e(mContext.getPackageName());
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(mContext, mContext.getPackageName()+".fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }
}

