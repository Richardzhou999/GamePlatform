package com.unis.gameplatfrom.ui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import com.trello.rxlifecycle2.android.ActivityEvent;


import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.adapter.GamesAdapter;
import com.unis.gameplatfrom.adapter.GamesRightAdapter;
import com.unis.gameplatfrom.api.HUDLoadDataSubscriber;
import com.unis.gameplatfrom.api.PublicApiInterface;
import com.unis.gameplatfrom.api.RetrofitWrapper;
import com.unis.gameplatfrom.api.result.BaseCustomListResult;
import com.unis.gameplatfrom.base.BaseActivity;
import com.unis.gameplatfrom.cache.InnerReceiver;

import com.unis.gameplatfrom.cache.NetConnectionReceiver;
import com.unis.gameplatfrom.cache.UserCenter;
import com.unis.gameplatfrom.model.GamesEntity;
import com.unis.gameplatfrom.ui.widget.MetroViewBorderImpl;
import com.unis.gameplatfrom.utils.DialogHelper;
import com.unis.gameplatfrom.utils.DownloadMgr;
import com.unis.gameplatfrom.utils.PackageUtil;
import com.unis.gameplatfrom.utils.download_mgr.DownloadTask;
import com.unis.gameplatfrom.utils.download_mgr.DownloadTaskListener;
import com.unis.gameplatfrom.utils.download_mgr.MyOkHttp;
import com.unis.gameplatfrom.utils.udateapk.DownLoadApkService;
import com.unis.gameplatfrom.utils.udateapk.DownloadAPk;
import com.unis.gameplatfrom.utils.udateapk.LinNotify;
import com.unis.gameplatfrom.utils.udateapk.LinPermission;


import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yong
 * on 2018/6/20.
 */


public class GamesActivity extends BaseActivity {

    @BindView(R.id.layout_game)
    LinearLayout gameLayout;


    @BindView(R.id.toolbar_left)
    TextView toolbarLeft;


    @BindView(R.id.rv_games)
    RecyclerView gamesRecycler;




    @BindView(R.id.back)
    ImageView mGameBack;



    public static int mProgress;

    private GamesAdapter adapter;
    private GamesRightAdapter rightAdapter;

    private List<GamesEntity> games;

    private List<GamesEntity> getGames = new ArrayList<>();


    private List<GamesEntity> gamesPage = new ArrayList<>();

    private static final String APK_URL = "http://101.28.249.94/apk.r1.market.hiapk.com/data/upload/apkres/2017/4_11/15/com.baidu.searchbox_034250.apk";

    private ProgressBar mProgressBar;


    private String BaseUrl = "http://s.health.shiyugame.com";

    private String account;
    private String password;

    private String game_account;

    private int page = 1;

    public boolean cancle;

    private View FooterView;

    private MetroViewBorderImpl mMetroViewBorderImpl;

    private String saveDownName = "";


    private List<Integer> progressList = new ArrayList<>();

    private boolean refresh = false;
    private LinearLayoutManager layoutManager;

    private MyOkHttp myOkHttp;
    private DownloadMgr mDownloadMgr;
    private DownloadTask mDownloadTask;
    private DownloadTaskListener mDownloadTaskListener;

    private boolean mReceiverTag = false;   //广播接受者标识
    private boolean mConnectTag;            //防止刷新列表数据


    private  InnerReceiver innerReceiver;
    private ItemNetConnectionReceiver itemNetConnectionReceiver;
    private NetConnectionReceiver netConnectionReceiver;
    private  IntentFilter mFilter;


    @Override
    protected int getLayout() {
        return R.layout.activity_games;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {


        FooterView = getLayoutInflater().inflate(R.layout.item_footer,null);

        //左边
        games = new ArrayList<>();
        adapter = new GamesAdapter(mContext,games);
        layoutManager = new LinearLayoutManager(this);
        gamesRecycler.setLayoutManager(layoutManager);
        gamesRecycler.getItemAnimator().setAddDuration(0);
        gamesRecycler.getItemAnimator().setChangeDuration(0);
        gamesRecycler.getItemAnimator().setMoveDuration(0);
        gamesRecycler.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) gamesRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter.addFooterView(FooterView);
        mMetroViewBorderImpl.attachTo(gamesRecycler);
        gamesRecycler.setAdapter(adapter);
        LitePal.getDatabase(); //创建数据表

        myOkHttp = new MyOkHttp();
        mDownloadMgr = (DownloadMgr) new DownloadMgr.Builder()
                .myOkHttp(myOkHttp)
                .maxDownloadIngNum(5)       //设置最大同时下载数量（不设置默认5）
                .saveProgressBytes(50 * 1024)  //设置每50kb触发一次saveProgress保存进度 （不能在onProgress每次都保存 过于频繁） 不设置默认50kb
                .build();

        innerReceiver = new InnerReceiver(mDownloadMgr);
        IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(innerReceiver, filter);

        itemNetConnectionReceiver = new ItemNetConnectionReceiver();
        mFilter = new IntentFilter();
        mFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(itemNetConnectionReceiver, mFilter);




        //layoutManager.setStackFromEnd(true);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                GamesEntity entity = (GamesEntity) adapter.getItem(position);

                if (entity.getV() != 0) {

                        if (LinPermission.checkPermission(GamesActivity.this, new int[]{7,8})) {

//                            if (mDownloadBinder != null) {
//                                long downloadId = mDownloadBinder.startDownload(APK_URL);
//                                startCheckProgress(downloadId);
//                            }


                            /**
                             * 情况1：记录不在，游戏在
                             * 情况2：记录不在，游戏不在
                             * 情况3：两者都在
                             */
                            GamesEntity entity1 = LitePal.where("id="+entity.getId()).findFirst(GamesEntity.class);
                            if(entity1 != null){

                                //若游戏被删除，需清除游戏记录防止数据出错
                                if (isAppByPackageID(entity.getPackname())) {


                                    System.out.print(entity.getV()+"");
                                    int number = entity.getV();

                                    if(number > entity1.getV()){

                                        //String content = String.format("发现新版本:V%s\n%s", entity., result.getData().getUpdateContent());
//                                        DialogHelper.showAlertDialog(mContext, "发现新版本", "立即更新", "暂不更新", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                dialogInterface.dismiss();
//                                                entity1.setV(entity.getV());
//                                                entity1.save();
//                                                downApk(entity.getP(),entity.getIcon());
//
//                                            }
//                                        }, new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                dialogInterface.dismiss();
//                                                startAppByPackageID(entity.getPackname());
//                                            }
//                                        });



                                        entity1.setV(entity.getV());
                                        entity.setGameId(entity.getId());
                                        UserCenter.getInstance().deleteGameFile(entity.getPackname());
                                        downApk(entity.getName(),entity.getP(),entity.getIcon(),
                                                entity,position);
                                        entity1.save();

                                    }else {

                                        entity.setDownGame(false);
                                        entity.setGameId(entity.getId());
                                        startAppByPackageID(entity.getPackname(),entity.getId());

                                    }



                                }else {

                                        entity1.setV(entity.getV());
                                        entity1.setId(entity.getId());
                                        entity.setGameId(entity.getId());
                                        entity1.setName(entity.getName());
                                        entity1.setP(entity.getP());
                                        entity1.setPackname(entity.getPackname());
                                        entity1.setIcon(entity.getIcon());
                                        entity1.save();
                                        //entity.setDownGame(true);
                                        downApk(entity.getName(), entity.getP(), entity.getIcon(),
                                                entity,position);



//                                    DialogHelper.showAlertDialog(mContext,"确定要下载吗", "确定", "取消", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            dialogInterface.dismiss();
//
//                                            entity.setV(entity.getV());
//                                            entity.save();
//                                            downApk(entity.getP(),entity.getIcon());
//
//                                        }
//                                    }, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            dialogInterface.dismiss();
//                                        }
//                                    });


                                }


                            }else {


                                if(isAppByPackageID(entity.getPackname())){

                                    entity.setAccount(game_account);
                                    entity.setGameId(entity.getId());
                                    entity.setDownGame(false);
                                    startAppByPackageID(entity.getPackname(),entity.getId());
                                    entity.save();
                                }else {

                                    //第一次下载

                                    entity.setAccount(game_account);
                                    entity.setGameId(entity.getId());
                                    //entity.setDownGame(true);
                                    downApk(entity.getName(),entity.getP(),entity.getIcon(),entity,position);
                                    entity.save();

                                }



//                                    DialogHelper.showAlertDialog(mContext,"确定要下载吗", "确定", "取消", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            dialogInterface.dismiss();
//                                            entity.save();
//                                            downApk(entity.getP(),entity.getIcon());
//
//                                        }
//                                    }, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            dialogInterface.dismiss();
//                                        }
//                                    });
                            }


                        }else {
                            //申请存储权限
                            LinPermission.requestPermission(GamesActivity.this, 7);
                            DialogHelper.showAlertDialog(mContext,"确定要下载吗", "确定", "取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    ///downApk(entity.getName(),entity.getP(),entity.getIcon());
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                        }
                }
            }
        });




    }


    @OnClick({R.id.back})
    public void onClick(View view){

        switch (view.getId()){

            case R.id.back:

                if (!DownGame) {
                    startActivity(new Intent(GamesActivity.this,MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(mContext, saveDownName + "  正在下载, 请勿退出", Toast.LENGTH_SHORT).show();
                }

                break;
           default:
               break;

        }
    }


    @Override
    protected void initView(Bundle savedInstanceState) {




        //创建通道
        LinNotify.setNotificationChannel(GamesActivity.this);
        StringBuilder builder = new StringBuilder();
        builder.append("厅大戏游");
        StringBuilder builder1 = new StringBuilder();
        builder1.append("厅大戏游");


        account = getIntent().getStringExtra("account");
        password = getIntent().getStringExtra("password");

        game_account= UserCenter.getInstance().getGame_account();

        gameLayout.getBackground().setAlpha(30);




        mMetroViewBorderImpl = new MetroViewBorderImpl(this,false);
        mMetroViewBorderImpl.setBackgroundResource(R.drawable.border_color);

        mGameBack.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mGameBack.setSelected(true);
                }else {
                    mGameBack.setSelected(false);
                }
            }
        });


    }



    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    protected void onResume() {
        super.onResume();

        if(NetworkUtils.isConnected()){
            mGameBack.setFocusable(false);
            mGameBack.setFocusableInTouchMode(false);
            loadData();
//        gameLayout.setFocusable(false);
//        gameLayout.setFocusableInTouchMode(false);

            gamesRecycler.setFocusable(true);
            gamesRecycler.setFocusableInTouchMode(true);
            if(!refresh)
                gamesRecycler.requestFocus();
        }else {

            ToastUtils.showShort("当前网络异常,请检查网络.网络无异常将自动刷新列表");

        }




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(innerReceiver);
        unregisterReceiver(itemNetConnectionReceiver);

    }

    //防止下载游戏中退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (!DownGame) {
                    finish();
                } else {
                    Toast.makeText(mContext, saveDownName + "  正在下载, 请勿退出", Toast.LENGTH_SHORT).show();
                }
                return true;//不执行父类点击事件
            }
            return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    private void loadData() {

        if(getGames.size() != 0 ){
            getGames.clear();
        }

        if(NetworkUtils.isConnected()) {
            RetrofitWrapper.getInstance().create(PublicApiInterface.class)
                    .getGameList(UserCenter.getInstance().getToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new HUDLoadDataSubscriber<BaseCustomListResult<GamesEntity>>(mContext) {
                        @Override
                        public void onNext(BaseCustomListResult<GamesEntity> result) {
                            mGameBack.setFocusable(true);
                            mGameBack.setFocusableInTouchMode(true);

                            if (result.getErr() == 0) {

                                if (LinPermission.checkPermission(GamesActivity.this, new int[]{7, 8})) {


                                    for (GamesEntity entity : result.getData()) {


                                        GamesEntity entity1 = LitePal.where("id=" + entity.getId()).findFirst(GamesEntity.class);
                                        if (entity1 != null) {
                                            // entity.setV(entity.getV()+1);

                                            if (PackageUtil.isAppByPackageID(mContext, entity.getPackname())) {


                                                if (entity.getV() > entity1.getV()) {

                                                    entity.setNewGame(true);
                                                    entity.setInstallGame(true);
                                                    entity.setLocal(true);
                                                    getGames.add(entity);

                                                } else {

                                                    entity.setLocal(true);
                                                    entity.setInstallGame(true);
                                                    getGames.add(entity);

                                                }


                                            } else {

                                                entity1.save();
                                                entity1.delete();

                                                if (PackageUtil.isAppByLocal(entity.getP())) {

                                                    entity.setNewGame(false);
                                                    entity.setInstallGame(false);
                                                    entity.setLocal(true);
                                                    getGames.add(entity);

                                                } else {

                                                    entity.setNewGame(false);
                                                    entity.setInstallGame(false);
                                                    entity.setLocal(false);
                                                    getGames.add(entity);

                                                }
                                            }

                                        } else {

                                            boolean appby = PackageUtil.isAppByPackageID(mContext, entity.getPackname());

                                            if (appby) {

                                                entity.setLocal(true);
                                                entity.setNewGame(false);
                                                entity.setInstallGame(true);
                                                getGames.add(entity);

                                            } else {


                                                if (PackageUtil.isAppByLocal(entity.getP())) {

                                                    entity.setNewGame(false);
                                                    entity.setInstallGame(false);
                                                    entity.setLocal(true);
                                                    getGames.add(entity);

                                                } else {

                                                    entity.setNewGame(false);
                                                    entity.setInstallGame(false);
                                                    entity.setLocal(false);
                                                    getGames.add(entity);

                                                }


                                            }

                                        }


                                    }

                                    if (!refresh) {

                                        refresh = true;
                                        adapter.setNewData(getGames);
                                    } else {

                                        if (mDownloadMgr != null) {
                                            mDownloadMgr.startAllTask();//开始所有下载任务
                                        }

                                    }

                                    FooterView.setVisibility(View.GONE);


                                } else {

//                            adapter.setNewData(result.getData());
//                            rightAdapter.setNewData(result.getData());
//                            mRefreshLayout.finishRefresh();

                                    //finish();
//                        adapter.addData(result.getData());

                                }

                            } else {

                                Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(mContext, LoginActivity.class));
                                finish();
                            }
                        }
                    });
        }

    }

    /**
     *  检查应用是否存在，若存在便打开
     */

    private boolean startAppByPackageID(String packageId,Integer gameid) {

        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo;
        try {
            // 应用包名
            packageInfo = packageManager.getPackageInfo( packageId, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(GamesActivity.this, "没有找到应用", Toast.LENGTH_SHORT).show();
            return false;
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageInfo.packageName);
        List<ResolveInfo> apps = packageManager.queryIntentActivities(resolveIntent, 0);
        ResolveInfo ri = apps.iterator().next();
        UserCenter.getInstance().save_gameId(mContext,gameid);
        if (ri != null && mDownloadMgr != null) {
            mDownloadMgr.pauseAllTask();//暂停所有下载任务
            String className = ri.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageInfo.packageName, className);
            intent.setComponent(cn);
            startActivity(intent);
        }
        return true;
    }


    /**
     * 检查应用是否存在
     * @param packageName
     */
    public boolean isAppByPackageID(String packageName) {

        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(
                    packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }






    private boolean DownGame = false;



    private void downApk(String name,String filepath,String iconUrl,GamesEntity entity,int positoin){


        if(filepath.contains("apk")){

            String[] apk_path = filepath.split("/");


            String path = Environment.getExternalStorageDirectory()
                    + "/DownLoad/apk/"+apk_path[apk_path.length-1];

            if(!entity.isDownGame() && !DownGame) {

                if (PackageUtil.isAppByLocal(filepath)) {
                    if(mDownloadMgr !=  null){
                        mDownloadMgr.pauseAllTask();//暂停所有下载任务
                        entity.setDownGame(false);
                        UserCenter.getInstance().save_gameId(mContext,entity.getGameId());
                        Intent installAppIntent = DownloadAPk.getInstallAppIntent(mContext, path);
                        startActivity(installAppIntent);

                    }

                } else {

                    if(NetworkUtils.isConnected()) {


                        DownGame = true;
                        mConnectTag = true;
                        progressList.add(positoin);
                        netConnectionReceiver = new NetConnectionReceiver(mDownloadMgr);
                        RegisterReceiver(netConnectionReceiver);
                        unregisterReceiver(itemNetConnectionReceiver);

                        //显示activity时加入监听
                        mDownloadTaskListener = new DownloadTaskListener() {
                            @Override
                            public void onStart(String taskId, long completeBytes, long totalBytes) {
                                mDownloadTask = mDownloadMgr.getDownloadTask(taskId);

                                saveDownName = entity.getName();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //netConnectionReceiver[0] = new NetConnectionReceiver(mDownloadMgr);
                                        //RegisterReceiver(netConnectionReceiver[0]);
                                    }
                                });

                            }

                            @Override
                            public void onProgress(String taskId, long currentBytes, long totalBytes) {
                                mDownloadTask = mDownloadMgr.getDownloadTask(taskId);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        for (int positoin : progressList) {
                                            //ps:建议不要每次刷新 可以通过handler postDelay延时刷新 防止刷新频率过快
                                            int progress = (int) (((float) currentBytes / totalBytes) * 100);

                                            GamesEntity gamesEntity = adapter.getItem(positoin);
                                            gamesEntity.setProgress(progress);
                                            gamesEntity.setDownGame(true);
                                            gamesEntity.setInstallGame(false);
                                            gamesEntity.setLocal(false);
                                            adapter.notifyItemChanged(positoin);

                                        }
                                    }
                                });
                            }

                            @Override
                            public void onPause(String taskId, long currentBytes, long totalBytes) {
                                mDownloadTask = mDownloadMgr.getDownloadTask(taskId);
                            }

                            @Override
                            public void onFinish(String taskId, File file) {
                                mDownloadTask = mDownloadMgr.getDownloadTask(taskId);
                                DownGame = false;
                                mDownloadMgr.removeListener(mDownloadTaskListener);
                                if (progressList.size() != 0) {
                                    progressList.clear();
                                }

                                mConnectTag = false;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        UnregisterReceiver(netConnectionReceiver);
                                        registerReceiver(itemNetConnectionReceiver, mFilter);

                                        GamesEntity gamesEntity = adapter.getItem(positoin);
                                        gamesEntity.setDownGame(false);
                                        gamesEntity.setInstallGame(true);
                                        gamesEntity.setLocal(true);
                                        adapter.notifyItemChanged(positoin);
                                        UserCenter.getInstance().save_gameId(mContext,entity.getGameId());
                                        Intent installAppIntent = DownloadAPk.getInstallAppIntent(mContext, path);
                                        startActivity(installAppIntent);

                                    }
                                });
                            }

                            @Override
                            public void onFailure(String taskId, String error_msg) {
                                mDownloadTask = mDownloadMgr.getDownloadTask(taskId);
                            }
                        };

                        mDownloadMgr.addListener(mDownloadTaskListener);

                        DownloadMgr.Task task = new DownloadMgr.Task();
                        task.setTaskId(mDownloadMgr.genTaskId() + entity.getName());       //生成一个taskId
                        task.setUrl(entity.getP());   //下载地址
                        task.setFilePath(path);    //下载后文件保存位置
                        task.setDefaultStatus(DownloadMgr.DEFAULT_TASK_STATUS_START);       //任务添加后开始状态 如果不设置 默认任务添加后就自动开始

                        mDownloadTask = mDownloadMgr.addTask(task);

                    }else {

                        ToastUtils.showShort("当前网络异常,无法下载");

                    }



//                    DownloadAPk.downApk(GamesActivity.this, filepath, path, iconUrl,
//                            new DownloadAPk.GameProgressListener() {
//                                @Override
//                                public void getProgress(int progress) {
//
//                                    DownGame = true;
//                                    saveDownName = entity.getName();
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//
//                                            for (int positoin : progressList) {
//
//                                                GamesEntity gamesEntity = adapter.getItem(positoin);
//                                                gamesEntity.setProgress(progress);
//                                                gamesEntity.setDownGame(true);
//
//                                                //adapter.notifyItemChanged(positoin,positoin);
//                                                adapter.notifyItemChanged(positoin);
//
//                                            }
//
//
//                                        }
//                                    });
//
//
//                                }
//
//                                @Override
//                                public void endDown() {
//
//                                    DownGame = false;
//                                    if (progressList.size() != 0) {
//                                        progressList.clear();
//                                    }
//
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//
//                                            GamesEntity gamesEntity = adapter.getItem(positoin);
//                                            gamesEntity.setDownGame(false);
//                                            adapter.notifyItemChanged(positoin);
//
//                                            Intent installAppIntent = DownloadAPk.getInstallAppIntent(mContext, path);
//                                            startActivity(installAppIntent);
//
//                                        }
//                                    });
//
//
//                                }
//                            });

                }

            }else{

                Toast.makeText(mContext, saveDownName+"  正在下载", Toast.LENGTH_SHORT).show();

            }


        }else {

            Toast.makeText(mContext,"游戏未上传,请跟客服人员联系",Toast.LENGTH_SHORT).show();
            entity.setDownGame(false);

        }



    }

    //态注册广播
    private void RegisterReceiver(NetConnectionReceiver mReceiver) {
        if (!mReceiverTag){     //在注册广播接受者的时候 判断是否已被注册,避免重复多次注册广播
            IntentFilter mFilter = new IntentFilter();
            mReceiverTag = true;    //标识值 赋值为 true 表示广播已被注册
            mFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(mReceiver, mFilter);
        }
    }

    //注销广播
    private void UnregisterReceiver(NetConnectionReceiver mReceiver) {
        if (mReceiverTag) {   //判断广播是否注册
            mReceiverTag = false;   //Tag值 赋值为false 表示该广播已被注销
            unregisterReceiver(mReceiver);   //注销广播
        }

    }


    public class ItemNetConnectionReceiver extends BroadcastReceiver {



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
                    Toast.makeText(context, "游戏大厅网络异常,请检查网络", Toast.LENGTH_SHORT).show();
                    mConnectTag = true;
//                    if(mDownloadMgr != null ){
//
//                        mDownloadMgr.pauseAllTask();//暂停所有下载任务
//                    }

                } else {
                    //网络连接已连接

                    if(mConnectTag){
                        mConnectTag = false;
                        refresh = false;
                        loadData();
                    }

//                    if(mDownloadMgr != null ){
//                        mDownloadMgr.startAllTask();//继续所有下载任务
//                    }


                }

            }

        }
    }





}
