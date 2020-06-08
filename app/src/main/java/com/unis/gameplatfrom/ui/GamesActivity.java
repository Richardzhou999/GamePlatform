package com.unis.gameplatfrom.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.trello.rxlifecycle2.android.ActivityEvent;


import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import com.unis.gameplatfrom.Constant;
import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.adapter.GamesAdapter;
import com.unis.gameplatfrom.adapter.GamesRightAdapter;
import com.unis.gameplatfrom.api.HUDLoadDataSubscriber;
import com.unis.gameplatfrom.api.PublicApiInterface;
import com.unis.gameplatfrom.api.RetrofitWrapper;
import com.unis.gameplatfrom.api.SimpleDataSubscriber;
import com.unis.gameplatfrom.api.result.BaseCustomListResult;
import com.unis.gameplatfrom.base.BaseActivity;
import com.unis.gameplatfrom.base.BaseToolBarActivity;
import com.unis.gameplatfrom.cache.UserCenter;
import com.unis.gameplatfrom.model.GamesEntity;
import com.unis.gameplatfrom.model.LoginResult;
import com.unis.gameplatfrom.ui.view.CustomText;
import com.unis.gameplatfrom.utils.DialogHelper;
import com.unis.gameplatfrom.utils.PackageUtil;
import com.unis.gameplatfrom.utils.udateapk.DownLoadApkService;
import com.unis.gameplatfrom.utils.udateapk.DownloadAPk;
import com.unis.gameplatfrom.utils.udateapk.LinNotify;
import com.unis.gameplatfrom.utils.udateapk.LinPermission;


import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.unis.gameplatfrom.utils.udateapk.DownloadAPk.APK_UPGRADE;


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
    RecyclerView gamesRv;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;


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




//        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//
//                loadData();
//
//            }
//        });



        mRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                loadData();

            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
               // refreshlayout.setRefreshFooter()
                FooterView.setVisibility(View.VISIBLE);
                loadData();

            }
        });




    }




    @OnClick({R.id.back})
    public void onClick(View view){

        switch (view.getId()){

            case R.id.back:
                startActivity(new Intent(GamesActivity.this,MainActivity.class));
                finish();

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


//        toolbarLeft.setRotation(180);
//        toolbarLeft.setText(toolbarLeft.ReversalByString("游戏大厅"));
        //toolbarLeft.setText("游戏大厅");

        //toolbarRight.setText(toolbarRight.ReversalByString("游戏"));



//        toolbarLeft.setText(builder1.reverse().toString());
//        toolbarRight.setText(builder.reverse().toString());

//        if (UserCenter.getInstance().isLogin()) {
//            rlayoutLogin.setVisibility(View.GONE);
//        } else {
//            rlayoutLogin.setVisibility(View.VISIBLE);
//        }

//        Intent intent = new Intent(this, DownloadService.class);
//        startService(intent);
//        bindService(intent, mConnection, BIND_AUTO_CREATE);//绑定服务


    }


//    private ServiceConnection mConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            mDownloadBinder = (DownLoadApkService.DownloadBinder) service;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mDownloadBinder = null;
//        }
//    };





    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }




    @Override
    protected void registEventBus() {

       // EventBus.getDefault().register(this);
    }

    @Override
    protected void unRegistEventBus() {

       // EventBus.getDefault().unregister(this);
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

                            if (LinPermission.checkPermission(GamesActivity.this, new int[]{7,8})) {


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

                                if(mRefreshLayout.isRefreshing()){
                                    mRefreshLayout.finishRefresh();
                                }
                                if(mRefreshLayout.isLoading()){
                                    mRefreshLayout.finishLoadmore();
                                    FooterView.setVisibility(View.GONE);
                                }



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
                            if(mRefreshLayout.isRefreshing()){
                                mRefreshLayout.finishRefresh();
                            }
                            if(mRefreshLayout.isLoading()){
                                mRefreshLayout.finishLoadmore();
                            }
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
            Toast.makeText(GamesActivity.this, "没有找到应用", Toast.LENGTH_SHORT).show();
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


                    progressList.add(positoin);

                    DownloadAPk.downApk(GamesActivity.this, filepath, path, iconUrl,
                            new DownloadAPk.GameProgressListener() {
                                @Override
                                public void getProgress(int progress) {

                                    DownGame = true;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            for (int positoin : progressList) {

                                                GamesEntity gamesEntity = adapter.getItem(positoin);
                                                gamesEntity.setProgress(progress);
                                                gamesEntity.setDownGame(true);

                                                //adapter.notifyItemChanged(positoin,positoin);
                                                adapter.notifyItemChanged(positoin);

                                            }


                                        }
                                    });


                                }

                                @Override
                                public void endDown() {

                                    DownGame = false;
                                    if (progressList.size() != 0) {
                                        progressList.clear();
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            GamesEntity gamesEntity = adapter.getItem(positoin);
                                            gamesEntity.setDownGame(false);
                                            adapter.notifyItemChanged(positoin);

                                            Intent installAppIntent = DownloadAPk.getInstallAppIntent(mContext, path);
                                            startActivity(installAppIntent);

                                        }
                                    });


                                }
                            });

                }

            }else{

                Toast.makeText(mContext, entity.getName()+"游戏正在下载", Toast.LENGTH_SHORT).show();

            }


        }else {

            Toast.makeText(mContext,"游戏未上传,请跟客服人员联系",Toast.LENGTH_SHORT).show();
            entity.setDownGame(false);

        }



    }


    //开始监听进度
    private void startCheckProgress(long downloadId) {
        Observable
                .interval(100, 200, TimeUnit.MILLISECONDS, Schedulers.io())//无限轮询,准备查询进度,在io线程执行
                .filter(times -> mDownloadBinder != null)
                //.map(i -> mDownloadBinder.getProgress(downloadId))//获得下载进度
                .takeUntil(progress -> progress >= 100)//返回true就停止了,当进度>=100就是下载完成了
                .distinct()//去重复
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<? super Long>) new ProgressObserver());
    }

    //观察者
    private class ProgressObserver implements Observer<Integer> {

        @Override
        public void onSubscribe(Disposable d) {
            mDisposable = d;
        }

        @Override
        public void onNext(Integer progress) {
           // mProgressBar.setProgress(progress);//设置进度
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
            //Toast.makeText(MainActivity.this, "出错", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete() {
            mProgressBar.setProgress(100);
            //Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
        }
    }

}
