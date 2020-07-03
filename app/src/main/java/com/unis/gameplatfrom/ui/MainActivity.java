package com.unis.gameplatfrom.ui;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.icu.lang.UCharacter;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.blankj.utilcode.util.EncryptUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.download_mgr.DownloadTask;
import com.tsy.sdk.myokhttp.download_mgr.DownloadTaskListener;
import com.unis.gameplatfrom.BuildConfig;
import com.unis.gameplatfrom.Constant;
import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.adapter.GamesAdapter;
import com.unis.gameplatfrom.adapter.MainAdapter;
import com.unis.gameplatfrom.api.HUDLoadDataSubscriber;
import com.unis.gameplatfrom.api.PublicApiInterface;
import com.unis.gameplatfrom.api.RetrofitWrapper;
import com.unis.gameplatfrom.api.result.BaseCustomListResult;
import com.unis.gameplatfrom.base.BaseActivity;
import com.unis.gameplatfrom.cache.InnerReceiver;
import com.unis.gameplatfrom.cache.NetConnectionReceiver;
import com.unis.gameplatfrom.cache.UserCenter;
import com.unis.gameplatfrom.model.GamesEntity;
import com.unis.gameplatfrom.model.LoginResult;
import com.unis.gameplatfrom.ui.widget.MetroItemFrameLayout;
import com.unis.gameplatfrom.ui.widget.MetroViewBorderHandler;
import com.unis.gameplatfrom.ui.widget.MetroViewBorderImpl;
import com.unis.gameplatfrom.ui.widget.SWRecyclerView;
import com.unis.gameplatfrom.utils.DialogHelper;
import com.unis.gameplatfrom.utils.DownloadMgr;
import com.unis.gameplatfrom.utils.PackageUtil;
import com.unis.gameplatfrom.utils.TimeUtil;
import com.unis.gameplatfrom.utils.udateapk.DownloadAPk;
import com.unis.gameplatfrom.utils.udateapk.LinPermission;

import org.litepal.LitePal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;



public class MainActivity extends BaseActivity {


    @BindView(R.id.user_image)
    ImageView userImage;

    @BindView(R.id.user_name)
    TextView userNameText;

    @BindView(R.id.rv_main)
    SWRecyclerView mainRecycler;


    @BindView(R.id.txt_month_week)
    TextView monthWeekText;

    @BindView(R.id.txt_now_time)
    TextView nowTimeText;

    @BindView(R.id.txt_version)
    TextView versionText;

    @BindView(R.id.movie_image)
    VideoView videoDetails;

    @BindView(R.id.iv_first_frame)
    ImageView firstImage;

    @BindView(R.id.movie_play)
    ImageView moviePlay;


    @BindView(R.id.left_layout)
    RelativeLayout leftLayout;

    @BindView(R.id.login_out)
    TextView mLoginOut;



    @BindView(R.id.push_layout)
    LinearLayout mPushLayout;

    @BindView(R.id.push_image)
    ImageView mPushImage;

    @BindView(R.id.movie_layout)
    RelativeLayout mMovieLayout;


    @BindView(R.id.into_game)
    ImageView IntoAllGame;

    @BindView(R.id.rv_layout)
    RelativeLayout mRecyclerLayout;




    private MainAdapter mainAdapter;

    private List<GamesEntity> gamesEntities  = new ArrayList<>();

    private MainAdapter adapter;


    private String userName;
    private String userHead;

    private String game_account;


    private MediaController localMediaController;

    private MetroViewBorderImpl mMetroViewBorderImpl;

    private boolean movie;
    private boolean fristplay;

    private MyOkHttp myOkHttp;
    private DownloadMgr mDownloadMgr;
    private DownloadTask mDownloadTask;
    private DownloadTaskListener mDownloadTaskListener;


    private boolean refresh = false;

    private boolean mReceiverTag = false;   //广播接受者标识

    private String saveDownName = "";

    private  InnerReceiver innerReceiver;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm");// HH:mm:ss
            //获取当前时间
            Date date1 = new Date(System.currentTimeMillis());
            nowTimeText.setText(TimeFormat.format(date1));

        }
    };


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    private List<Integer> progressList = new ArrayList<>();


    @Override
    protected void initData() {

        adapter = new MainAdapter(mContext,gamesEntities);
        mainRecycler.setLayoutManager(new LinearLayoutManager(this));
        mainRecycler.getItemAnimator().setAddDuration(0);
        mainRecycler.getItemAnimator().setChangeDuration(0);
        mainRecycler.getItemAnimator().setMoveDuration(0);
        mainRecycler.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) mainRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        mMetroViewBorderImpl.attachTo(mainRecycler);

        mainRecycler.setAdapter(adapter);
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


        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                GamesEntity  entity = (GamesEntity) adapter.getItem(position);

                if (entity.getV() != 0) {

                    if (LinPermission.checkPermission(MainActivity.this, new int[]{7,8})) {

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
                            if (PackageUtil.isAppByPackageID(mContext,entity.getPackname())) {


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

                                    PackageUtil.startAppByPackageID(mContext,entity.getPackname(),mDownloadMgr);

                                }



                            }else {

                                entity1.setV(entity.getV());
                                entity1.setId(entity.getId());
                                entity1.setName(entity.getName());
                                entity1.setP(entity.getP());
                                entity1.setPackname(entity.getPackname());
                                entity1.setIcon(entity.getIcon());
                                entity1.save();
                                downApk(entity.getName(),entity.getP(),entity.getIcon(),
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


                            if(PackageUtil.isAppByPackageID(mContext,entity.getPackname())){

                                entity.setAccount(game_account);
                                entity.save();
                                PackageUtil.startAppByPackageID(mContext,entity.getPackname(),mDownloadMgr);

                            }else {

                                //第一次下载

                                entity.setAccount(game_account);
                                entity.save();
                                downApk(entity.getName(),entity.getP(),entity.getIcon(),
                                        entity,position);


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

                    }
                }
            }
        });




    }

    @Override
    protected void initView(Bundle savedInstanceState) {


        userName =  UserCenter.getInstance().getUserName();
        userHead =   UserCenter.getInstance().getUserHead();

        moviePlay.setVisibility(View.VISIBLE);
        firstImage.setVisibility(View.VISIBLE);





        //mRecyclerLayout.setFocusable(true);



        userNameText.setText(userName);
        if(BuildConfig.DEBUG){

            versionText.setText("版本号： V"+Constant.DEBUG);

        }else {

            versionText.setText("版本号： V"+PackageUtil.getVersionName(mContext));

        }

        //
        Glide.with(mContext).load(userHead)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(userImage);




        SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm");// HH:mm:ss
        //获取当前时间
        Date date1 = new Date(System.currentTimeMillis());

        monthWeekText.setText(TimeUtil.StringData());
        nowTimeText.setText(TimeFormat.format(date1));

        new TimeThread().start();


        //设置有进度条可以拖动快进
        String url = "http://oa47.oss-cn-shenzhen.aliyuncs.com/2.mp4";
         localMediaController = new MediaController(this);
        //拿到MediaController
        videoDetails.setMediaController(localMediaController);
        //先加载地址
        videoDetails.setVideoPath(url);
        //localMediaController.show();

        firstImage.setImageBitmap(getVideoThumb(url));

        mMetroViewBorderImpl = new MetroViewBorderImpl(this,true);
        mMetroViewBorderImpl.setBackgroundResource(R.drawable.border_color);



        leftLayout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.game_toolbar));

        mLoginOut.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                    mLoginOut.setSelected(true);
                    mLoginOut.setTextColor(ContextCompat.getColor(mContext,R.color.white));

                }else {
                    mLoginOut.setSelected(false);
                    mLoginOut.setTextColor(ContextCompat.getColor(mContext,R.color.out_login_txt));
                }
            }
        });





        mPushLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                    mPushLayout.setBackgroundResource(R.drawable.border_color);
                }else {
                    mPushLayout.setBackgroundResource(android.R.color.transparent);
                }
              }
        });

        mMovieLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                    mMovieLayout.setBackgroundResource(R.drawable.border_color);
                }else {

                    mMovieLayout.setBackgroundResource(android.R.color.transparent);
                }
            }
        });


        // finish();

    }




    @Override
    protected void onResume() {
        super.onResume();
        videoDetails.setFocusable(false);
        videoDetails.setFocusableInTouchMode(false);
        loadData();

        mainRecycler.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        mPushLayout.setFocusable(false);
        mPushLayout.setFocusableInTouchMode(false);
        mLoginOut.setFocusable(false);
        mLoginOut.setFocusableInTouchMode(false);
        mMovieLayout.setFocusable(false);
        mMovieLayout.setFocusableInTouchMode(false);
        mainRecycler.setFocusable(true);
        mainRecycler.setFocusableInTouchMode(true);

        if(!refresh) {
            mainRecycler.requestFocus();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(innerReceiver);


    }

    private void loadData() {


        if(gamesEntities.size() != 0 ){
            gamesEntities.clear();
        }

        game_account = UserCenter.getInstance().getGame_account();

        RetrofitWrapper.getInstance().create(PublicApiInterface.class)
                .getGameList(UserCenter.getInstance().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new HUDLoadDataSubscriber<BaseCustomListResult<GamesEntity>>(mContext) {
                    @Override
                    public void onNext(BaseCustomListResult<GamesEntity> result) {


                        IntoAllGame.setFocusable(true);
                        mPushLayout.setFocusable(true);
                        mLoginOut.setFocusable(true);
                        mMovieLayout.setFocusable(true);

                        if(result.getErr() == 0 && result.getData().size() != 0){

                            if (LinPermission.checkPermission(MainActivity.this, new int[]{7,8})) {

                                if(result.getData().size() < 3){


                                    for(int i = 0 ; i < result.getData().size() ; i ++){



                                        GamesEntity entity = result.getData().get(i);

                                        GamesEntity entity1 = LitePal.where("id="+entity.getId()).findFirst(GamesEntity.class);
                                        if( entity1 != null ){
                                            // entity.setV(entity.getV()+1);

                                            if(PackageUtil.isAppByPackageID(mContext,entity.getPackname())){



                                                if(entity.getV() > entity1.getV()){

                                                    entity.setNewGame(true);
                                                    entity.setInstallGame(true);
                                                    entity.setLocal(true);
                                                    gamesEntities.add(entity);

                                                }else {

                                                    entity.setLocal(true);
                                                    entity.setInstallGame(true);
                                                    gamesEntities.add(entity);

                                                }


                                            }else {

                                                entity1.save();
                                                entity1.delete();

                                                if(PackageUtil.isAppByLocal(entity.getP())){

                                                    entity.setNewGame(false);
                                                    entity.setInstallGame(false);
                                                    entity.setLocal(true);
                                                    gamesEntities.add(entity);

                                                }else {

                                                    entity.setNewGame(false);
                                                    entity.setInstallGame(false);
                                                    entity.setLocal(false);
                                                    gamesEntities.add(entity);

                                                }
                                            }

                                        }else {

                                            if(PackageUtil.isAppByPackageID(mContext,entity.getPackname())){


                                                entity.setLocal(true);
                                                entity.setNewGame(false);
                                                entity.setInstallGame(true);
                                                gamesEntities.add(entity);

                                            }else {


                                                if(PackageUtil.isAppByLocal(entity.getP())){

                                                    entity.setNewGame(false);
                                                    entity.setInstallGame(false);
                                                    entity.setLocal(true);
                                                    gamesEntities.add(entity);

                                                }else {

                                                    entity.setNewGame(false);
                                                    entity.setInstallGame(false);
                                                    entity.setLocal(false);
                                                    gamesEntities.add(entity);

                                                }



                                            }

                                        }



                                    }


                                } else {

                                    for(int i = 0 ; i < 3 ; i ++){



                                        GamesEntity entity = result.getData().get(i);

                                        GamesEntity entity1 = LitePal.where("id="+entity.getId()).findFirst(GamesEntity.class);
                                        if( entity1 != null ){
                                            // entity.setV(entity.getV()+1);

                                            if(PackageUtil.isAppByPackageID(mContext,entity.getPackname())){



                                                if(entity.getV() > entity1.getV()){

                                                    entity.setNewGame(true);
                                                    entity.setInstallGame(true);
                                                    entity.setLocal(true);
                                                    gamesEntities.add(entity);

                                                }else {

                                                    entity.setLocal(true);
                                                    entity.setInstallGame(true);
                                                    gamesEntities.add(entity);

                                                }


                                            }else {

                                                entity1.save();
                                                entity1.delete();

                                                if(PackageUtil.isAppByLocal(entity.getP())){

                                                    entity.setNewGame(false);
                                                    entity.setInstallGame(false);
                                                    entity.setLocal(true);
                                                    gamesEntities.add(entity);

                                                }else {

                                                    entity.setNewGame(false);
                                                    entity.setInstallGame(false);
                                                    entity.setLocal(false);
                                                    gamesEntities.add(entity);

                                                }
                                            }

                                        }else {

                                            if(PackageUtil.isAppByPackageID(mContext,entity.getPackname())){


                                                entity.setLocal(true);
                                                entity.setNewGame(false);
                                                entity.setInstallGame(true);
                                                gamesEntities.add(entity);

                                            }else {


                                                if(PackageUtil.isAppByLocal(entity.getP())){

                                                    entity.setNewGame(false);
                                                    entity.setInstallGame(false);
                                                    entity.setLocal(true);
                                                    gamesEntities.add(entity);

                                                }else {

                                                    entity.setNewGame(false);
                                                    entity.setInstallGame(false);
                                                    entity.setLocal(false);
                                                    gamesEntities.add(entity);

                                                }
                                            }
                                        }
                                    }
                                }


                                if(!refresh){
                                    refresh = true;
                                    adapter.setNewData(gamesEntities);
                                }else {

                                    if(mDownloadMgr != null){
                                        mDownloadMgr.startAllTask();//开始所有下载任务
                                    }

                                }
                                //mainRecycler.smoothScrollToPosition(1);




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
                            finish();

                        }
                    }
                });





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

                final NetConnectionReceiver[] netConnectionReceiver = new NetConnectionReceiver[1];

                progressList.add(positoin);

                //显示activity时加入监听
                mDownloadTaskListener = new DownloadTaskListener() {
                    @Override
                    public void onStart(String taskId, long completeBytes, long totalBytes) {
                        mDownloadTask = mDownloadMgr.getDownloadTask(taskId);
                        DownGame = true;
                        saveDownName = entity.getName();
                        netConnectionReceiver[0] = new NetConnectionReceiver(mDownloadMgr);
                        RegisterReceiver(netConnectionReceiver[0]);
                    }

                    @Override
                    public void onProgress(String taskId, long currentBytes, long totalBytes) {
                        mDownloadTask = mDownloadMgr.getDownloadTask(taskId);

//                                  runOnUiThread(new Runnable() {
//                                      @Override
//                                      public void run() {

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
//                                      }
//                                  });
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


//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {

                        unregisterReceiver(netConnectionReceiver[0]);
                        GamesEntity gamesEntity = adapter.getItem(positoin);
                        gamesEntity.setDownGame(false);
                        gamesEntity.setInstallGame(true);
                        gamesEntity.setLocal(true);
                        adapter.notifyItemChanged(positoin);

                        Intent installAppIntent = DownloadAPk.getInstallAppIntent(mContext, path);
                        startActivity(installAppIntent);

//                                        }
//                                    });
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


        }



    }


    @OnClick({R.id.into_game,R.id.login_out,R.id.movie_layout})
    public void onViewClicked(View view){

        switch (view.getId()){

            case R.id.into_game:

                if (!DownGame) {
                    if (LinPermission.checkPermission(MainActivity.this, new int[]{7,8})) {
                        Intent intent = new Intent(MainActivity.this,GamesActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    Toast.makeText(mContext, saveDownName + "  正在下载, 请勿退出", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.login_out:

                if (!DownGame) {
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(mContext, saveDownName + "  正在下载, 请勿退出", Toast.LENGTH_SHORT).show();
                }



                break;

            case R.id.movie_layout:
                if(!fristplay){

                    moviePlay.setVisibility(View.GONE);
                    firstImage.setVisibility(View.GONE);
                    videoDetails.start();
                    fristplay = true;

                }else {

                    if(!movie){

                        moviePlay.setVisibility(View.VISIBLE);
                        videoDetails.pause();
                        movie = true;


                    }else {

                        moviePlay.setVisibility(View.GONE);
                        videoDetails.start();
                        movie = false;


                    }



                }

                break;



            default:
                break;





        }





    }


    /**

     * 获取视频文件截图

     *

     * @param url 视频文件的路径

     * @return Bitmap 返回获取的Bitmap

     */

    public Bitmap getVideoThumb(String url) {

        MediaMetadataRetriever media = new MediaMetadataRetriever();

        media.setDataSource(url,new HashMap<>());

        return  media.getFrameAtTime();

    }


    //态注册广播
    private void RegisterReceiver(NetConnectionReceiver mReceiver) {
        if (!mReceiverTag){     //在注册广播接受者的时候 判断是否已被注册,避免重复多次注册广播
            IntentFilter mFileter = new IntentFilter();
            mReceiverTag = true;    //标识值 赋值为 true 表示广播已被注册
            mFileter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            this.registerReceiver(mReceiver, mFileter);
        }
    }

    //注销广播
    private void unregisterReceiver(NetConnectionReceiver mReceiver) {
        if (mReceiverTag) {   //判断广播是否注册
            mReceiverTag = false;   //Tag值 赋值为false 表示该广播已被注销
            this.unregisterReceiver(mReceiver);   //注销广播
        }

    }


    /**
     * 开启一个线程，每个一秒钟更新时间
     */
    public class TimeThread extends Thread {
        //重写run方法
        @Override
        public void run() {
            super.run();

            do {
                try {
                    //每隔一秒 发送一次消息
                    Thread.sleep(1000*60);
                    Message msg = handler.obtainMessage();
                    //发送
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }



}
