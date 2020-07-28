package com.unis.gameplatfrom.ui;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.trello.rxlifecycle2.android.ActivityEvent;

import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.adapter.GamesAdapter;
import com.unis.gameplatfrom.adapter.GamesRightAdapter;
import com.unis.gameplatfrom.api.HUDLoadDataSubscriber;
import com.unis.gameplatfrom.api.PublicApiInterface;
import com.unis.gameplatfrom.api.RetrofitWrapper;
import com.unis.gameplatfrom.api.result.BaseCustomListResult;
import com.unis.gameplatfrom.base.BaseActivity;
import com.unis.gameplatfrom.cache.NetConnectionReceiver;
import com.unis.gameplatfrom.cache.UserCenter;
import com.unis.gameplatfrom.model.GamesEntity;
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

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yong
 * on 2018/6/20.
 */


public class Games2Activity extends BaseActivity {

    @BindView(R.id.layout_game)
    LinearLayout gameLayout;


    @BindView(R.id.toolbar_left)
    TextView toolbarLeft;


    @BindView(R.id.rv_games)
    RecyclerView gamesRv;

//    @BindView(R.id.refresh_layout)
//    SmartRefreshLayout mRefreshLayout;


    public static int mProgress;

    private GamesAdapter adapter;
    private GamesRightAdapter rightAdapter;

    private List<GamesEntity> games;

    private List<GamesEntity> getGames = new ArrayList<>();


    private List<GamesEntity> gamesPage = new ArrayList<>();

    private static final String APK_URL = "http://101.28.249.94/apk.r1.market.hiapk.com/data/upload/apkres/2017/4_11/15/com.baidu.searchbox_034250.apk";

    private ProgressBar mProgressBar;

    private DownLoadApkService.DownloadBinder mDownloadBinder;
    private Disposable mDisposable;//可以取消观察者

    private String BaseUrl = "http://s.health.shiyugame.com";

    private String account;
    private String password;

    private String game_account;

    private int page = 1;

    public boolean cancle;

    private View FooterView;


    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);




        }
    };


    private List<Integer> progressList = new ArrayList<>();

    private MyOkHttp myOkHttp;
    private DownloadMgr mDownloadMgr;
    private DownloadTask mDownloadTask;
    private DownloadTaskListener mDownloadTaskListener;


    private NetConnectionReceiver netConnectionReceiver;


    @Override
    protected int getLayout() {
        return R.layout.activity_games;
    }





    @Override
    protected void initData() {


        FooterView = getLayoutInflater().inflate(R.layout.item_footer,null);

        //左边
        games = new ArrayList<>();
        adapter = new GamesAdapter(mContext,games);
        gamesRv.setLayoutManager(new LinearLayoutManager(this));
        adapter.addFooterView(FooterView);

        gamesRv.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LitePal.getDatabase(); //创建数据表


                GamesEntity entity = (GamesEntity) adapter.getItem(position);

                if (entity.getV() != 0) {

                        if (LinPermission.checkPermission(Games2Activity.this, new int[]{7,8})) {

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

                                        entity1.save();
                                        downApk(entity.getName(),entity.getP(),entity.getIcon(),
                                                entity,position);


                                    }else {

                                        entity.setDownGame(false);
                                        startAppByPackageID(entity.getPackname());

                                    }



                                }else {

                                        entity1.setV(entity.getV());
                                        entity1.setId(entity.getId());
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
                                    entity.save();
                                    entity.setDownGame(false);
                                    startAppByPackageID(entity.getPackname());

                                }else {

                                    //第一次下载

                                    entity.setAccount(game_account);
                                    entity.save();
                                    //entity.setDownGame(true);
                                    downApk(entity.getName(),entity.getP(),entity.getIcon(),entity,position);


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

                        }
                }
            }
        });


//        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//
//                switch (view.getId()){
//                    //下载游戏
//                    case R.id.item_game_down:
//
//                        GamesEntity entity = (GamesEntity) adapter.getItem(position);
////
//                if (entity.getV() != 0) {
//
//                        if (LinPermission.checkPermission(Games2Activity.this, new int[]{7,8})) {
//
////                            if (mDownloadBinder != null) {
////                                long downloadId = mDownloadBinder.startDownload(APK_URL);
////                                startCheckProgress(downloadId);
////                            }
//
//
//                            /**
//                             * 情况1：记录不在，游戏在
//                             * 情况2：记录不在，游戏不在
//                             * 情况3：两者都在
//                             */
//                            GamesEntity entity1 = LitePal.where("id="+entity.getId()).findFirst(GamesEntity.class);
//                            if(entity1 != null){
//
//                                //若游戏被删除，需清除游戏记录防止数据出错
//                                if (isAppByPackageID(entity.getPackname())) {
//
//
//                                    System.out.print(entity.getV()+"");
//                                    int number = entity.getV();
//
//
//                                    if(number > entity1.getV()){
//                                        //String content = String.format("发现新版本:V%s\n%s", entity., result.getData().getUpdateContent());
//
//
//
////                                        DialogHelper.showAlertDialog(mContext, "发现新版本", "立即更新", "暂不更新", new DialogInterface.OnClickListener() {
////                                            @Override
////                                            public void onClick(DialogInterface dialogInterface, int i) {
////                                                dialogInterface.dismiss();
////                                                entity1.setV(entity.getV());
////                                                entity1.save();
////                                                downApk(entity.getP(),entity.getIcon());
////
////                                            }
////                                        }, new DialogInterface.OnClickListener() {
////                                            @Override
////                                            public void onClick(DialogInterface dialogInterface, int i) {
////                                                dialogInterface.dismiss();
////                                                startAppByPackageID(entity.getPackname());
////                                            }
////                                        });
//
//
//
//                                        entity1.setV(entity.getV());
//
//                                        entity1.save();
//                                        downApk(entity.getName(),entity.getP(),entity.getIcon(),
//                                                entity,position);
//
//
//                                    }else {
//
//                                        entity.setDownGame(false);
//                                        startAppByPackageID(entity.getPackname());
//
//                                    }
//
//
//
//                                }else {
//
//                                        entity1.setV(entity.getV());
//                                        entity1.setId(entity.getId());
//                                        entity1.setName(entity.getName());
//                                        entity1.setP(entity.getP());
//                                        entity1.setPackname(entity.getPackname());
//                                        entity1.setIcon(entity.getIcon());
//                                        entity1.save();
//                                        //entity.setDownGame(true);
//                                        downApk(entity.getName(), entity.getP(), entity.getIcon(),
//                                                entity,position);
//
//
////                                    DialogHelper.showAlertDialog(mContext,"确定要下载吗", "确定", "取消", new DialogInterface.OnClickListener() {
////                                        @Override
////                                        public void onClick(DialogInterface dialogInterface, int i) {
////                                            dialogInterface.dismiss();
////
////                                            entity.setV(entity.getV());
////                                            entity.save();
////                                            downApk(entity.getP(),entity.getIcon());
////
////                                        }
////                                    }, new DialogInterface.OnClickListener() {
////                                        @Override
////                                        public void onClick(DialogInterface dialogInterface, int i) {
////                                            dialogInterface.dismiss();
////                                        }
////                                    });
//
//
//                                }
//
//
//                            }else {
//
//
//                                if(isAppByPackageID(entity.getPackname())){
//
//                                    entity.setAccount(game_account);
//                                    entity.save();
//                                    entity.setDownGame(false);
//                                    startAppByPackageID(entity.getPackname());
//
//                                }else {
//
//                                    //第一次下载
//
//                                    entity.setAccount(game_account);
//                                    entity.save();
//                                    //entity.setDownGame(true);
//                                    downApk(entity.getName(),entity.getP(),entity.getIcon(),entity,position);
//
//
//                                }
//
//
//
////                                    DialogHelper.showAlertDialog(mContext,"确定要下载吗", "确定", "取消", new DialogInterface.OnClickListener() {
////                                        @Override
////                                        public void onClick(DialogInterface dialogInterface, int i) {
////                                            dialogInterface.dismiss();
////                                            entity.save();
////                                            downApk(entity.getP(),entity.getIcon());
////
////                                        }
////                                    }, new DialogInterface.OnClickListener() {
////                                        @Override
////                                        public void onClick(DialogInterface dialogInterface, int i) {
////                                            dialogInterface.dismiss();
////                                        }
////                                    });
//                            }
//
//
//                        }else {
//                            //申请存储权限
//                            LinPermission.requestPermission(Games2Activity.this, 7);
//                            DialogHelper.showAlertDialog(mContext,"确定要下载吗", "确定", "取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
//                                    ///downApk(entity.getName(),entity.getP(),entity.getIcon());
//                                }
//                            }, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
//                                }
//                            });
//                        }
//                }
//
//
//                        break;
//
//                    //取消游戏：
//                    case R.id.item_game_cancel:
//
//                        mDownloadMgr.deleteTask(mDownloadTask.getTaskId());
//
//                        //mDownloadTask.doPause();
//
//                        break;
//
//
//                }
//
//
//
//            }
//        });


        myOkHttp = new MyOkHttp();
        mDownloadMgr = (DownloadMgr) new DownloadMgr.Builder()
                .myOkHttp(myOkHttp)
                .maxDownloadIngNum(5)       //设置最大同时下载数量（不设置默认5）
                .saveProgressBytes(50 * 1024)  //设置每50kb触发一次saveProgress保存进度 （不能在onProgress每次都保存 过于频繁） 不设置默认50kb
                .build();












    }




    @OnClick({R.id.back})
    public void onClick(View view){

        switch (view.getId()){

            case R.id.back:

                startActivity(new Intent(Games2Activity.this,MainActivity.class));
                finish();

                break;
           default:
               break;






        }


    }


    @Override
    protected void initView(Bundle savedInstanceState) {




        //创建通道
        LinNotify.setNotificationChannel(Games2Activity.this);
        StringBuilder builder = new StringBuilder();
        builder.append("厅大戏游");
        StringBuilder builder1 = new StringBuilder();
        builder1.append("厅大戏游");


        account = getIntent().getStringExtra("account");
        password = getIntent().getStringExtra("password");

        game_account= UserCenter.getInstance().getGame_account();

        gameLayout.getBackground().setAlpha(30);




    }






    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }




    private void loadData() {

        if(getGames.size() != 0 ){
            getGames.clear();
        }




        RetrofitWrapper.getInstance().create(PublicApiInterface.class)
                .getGameList(UserCenter.getInstance().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new HUDLoadDataSubscriber<BaseCustomListResult<GamesEntity>>(mContext) {
                    @Override
                    public void onNext(BaseCustomListResult<GamesEntity> result) {


                        if(result.getErr() == 0){

                            if (LinPermission.checkPermission(Games2Activity.this, new int[]{7,8})) {


                                for(GamesEntity entity : result.getData()){


                                    GamesEntity entity1 = LitePal.where("id="+entity.getId()).findFirst(GamesEntity.class);
                                    if( entity1 != null ){
                                        // entity.setV(entity.getV()+1);

                                        if(isAppByPackageID(entity.getPackname())){



                                            if(entity.getV() > entity1.getV()){

                                                entity.setNewGame(true);
                                                entity.setInstallGame(true);
                                                entity.setLocal(true);
                                                getGames.add(entity);

                                            }else {

                                                entity.setLocal(true);
                                                entity.setInstallGame(true);
                                                getGames.add(entity);

                                            }


                                        }else {

                                            entity1.save();
                                            entity1.delete();

                                            if(PackageUtil.isAppByLocal(entity.getP())){

                                                entity.setNewGame(false);
                                                entity.setInstallGame(false);
                                                entity.setLocal(true);
                                                getGames.add(entity);

                                            }else {

                                                entity.setNewGame(false);
                                                entity.setInstallGame(false);
                                                entity.setLocal(false);
                                                getGames.add(entity);

                                            }
                                        }

                                    }else {

                                        if(isAppByPackageID(entity.getPackname())){


                                            entity.setLocal(true);
                                            entity.setNewGame(false);
                                            entity.setInstallGame(true);
                                            getGames.add(entity);

                                        }else {


                                            if(PackageUtil.isAppByLocal(entity.getP())){

                                                entity.setNewGame(false);
                                                entity.setInstallGame(false);
                                                entity.setLocal(true);
                                                getGames.add(entity);

                                            }else {

                                                entity.setNewGame(false);
                                                entity.setInstallGame(false);
                                                entity.setLocal(false);
                                                getGames.add(entity);

                                            }



                                        }

                                    }



                                }

                                adapter.setNewData(getGames);

//                                if(mRefreshLayout.isRefreshing()){
//                                    mRefreshLayout.finishRefresh();
//                                }
//                                if(mRefreshLayout.isLoading()){
//                                    mRefreshLayout.finishLoadmore();
//                                    FooterView.setVisibility(View.GONE);
//                                }



                            }
                            else {

//                            adapter.setNewData(result.getData());
//                            rightAdapter.setNewData(result.getData());
//                            mRefreshLayout.finishRefresh();

                                //finish();
//                        adapter.addData(result.getData());

                            }

                        }else {

                            Toast.makeText(mContext,result.getMsg(),Toast.LENGTH_SHORT).show();
//                            if(mRefreshLayout.isRefreshing()){
//                                mRefreshLayout.finishRefresh();
//                            }
//                            if(mRefreshLayout.isLoading()){
//                                mRefreshLayout.finishLoadmore();
//                            }
                        }
                    }
                });

    }

    /**
     *  检查应用是否存在，若存在便打开
     */

    private boolean startAppByPackageID(String packageId) {

        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo;
        try {
            // 应用包名
            packageInfo = packageManager.getPackageInfo( packageId, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(Games2Activity.this, "没有找到应用", Toast.LENGTH_SHORT).show();
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

                    entity.setDownGame(false);
                    Intent installAppIntent = DownloadAPk.getInstallAppIntent(mContext, path);
                    startActivity(installAppIntent);


                } else {


//                    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//                    netConnectionReceiver = new NetConnectionReceiver(mDownloadMgr);
//                    registerReceiver(netConnectionReceiver, filter);



                    progressList.add(positoin);

                    //显示activity时加入监听
                    mDownloadTaskListener = new DownloadTaskListener() {
                        @Override
                        public void onStart(String taskId, long completeBytes, long totalBytes) {
                            mDownloadTask = mDownloadMgr.getDownloadTask(taskId);

                        }

                        @Override
                        public void onProgress(String taskId, long currentBytes, long totalBytes) {
                            mDownloadTask = mDownloadMgr.getDownloadTask(taskId);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //ps:建议不要每次刷新 可以通过handler postDelay延时刷新 防止刷新频率过快
                                    int progress = (int) (((float)currentBytes/totalBytes) * 100);
                                    GamesEntity gamesEntity = adapter.getItem(positoin);
                                    gamesEntity.setProgress(progress);
                                    gamesEntity.setDownGame(true);

                                    adapter.notifyItemChanged(positoin,progress);
                                    //adapter.notifyItemChanged(positoin);


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

                        }

                        @Override
                        public void onFailure(String taskId, String error_msg) {
                            mDownloadTask = mDownloadMgr.getDownloadTask(taskId);
                        }
                    };

                    mDownloadMgr.addListener(mDownloadTaskListener);


                    DownloadMgr.Task task = new DownloadMgr.Task();
                    task.setTaskId(mDownloadMgr.genTaskId()+entity.getName());       //生成一个taskId
                    task.setUrl(entity.getP());   //下载地址
                    task.setFilePath(path);    //下载后文件保存位置
                    task.setDefaultStatus(DownloadMgr.DEFAULT_TASK_STATUS_START);       //任务添加后开始状态 如果不设置 默认任务添加后就自动开始

                    mDownloadTask = mDownloadMgr.addTask(task);




                }

            }else{

                Toast.makeText(mContext, entity.getName()+"游戏正在下载", Toast.LENGTH_SHORT).show();

            }


        }else {

            Toast.makeText(mContext,"游戏未上传,请跟客服人员联系",Toast.LENGTH_SHORT).show();
            entity.setDownGame(false);

        }



    }




}
