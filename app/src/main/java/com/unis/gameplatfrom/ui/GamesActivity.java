package com.unis.gameplatfrom.ui;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.trello.rxlifecycle2.android.ActivityEvent;


import org.litepal.LitePal;

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
import com.unis.gameplatfrom.cache.UserCenter;
import com.unis.gameplatfrom.model.GamesEntity;
import com.unis.gameplatfrom.ui.widget.MetroViewBorderImpl;
import com.unis.gameplatfrom.utils.DialogHelper;
import com.unis.gameplatfrom.utils.PackageUtil;
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

    private DownLoadApkService.DownloadBinder mDownloadBinder;
    private Disposable mDisposable;//可以取消观察者

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


        //layoutManager.setStackFromEnd(true);

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
        mGameBack.setFocusable(false);
        mGameBack.setFocusableInTouchMode(false);
        loadData();
//        gameLayout.setFocusable(false);
//        gameLayout.setFocusableInTouchMode(false);

        gamesRecycler.setFocusable(true);
        gamesRecycler.setFocusableInTouchMode(true);
        if(!refresh)
        gamesRecycler.requestFocus();



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

                                if(!refresh){

                                    refresh = true;
                                    adapter.setNewData(getGames);
                                }

                                FooterView.setVisibility(View.GONE);




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
                                    saveDownName = entity.getName();
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

                Toast.makeText(mContext, saveDownName+"  正在下载", Toast.LENGTH_SHORT).show();

            }


        }else {

            Toast.makeText(mContext,"游戏未上传,请跟客服人员联系",Toast.LENGTH_SHORT).show();
            entity.setDownGame(false);

        }



    }




}
