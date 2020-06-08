package com.unis.gameplatfrom.ui;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.lang.UCharacter;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.unis.gameplatfrom.BuildConfig;
import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.adapter.GamesAdapter;
import com.unis.gameplatfrom.adapter.MainAdapter;
import com.unis.gameplatfrom.api.HUDLoadDataSubscriber;
import com.unis.gameplatfrom.api.PublicApiInterface;
import com.unis.gameplatfrom.api.RetrofitWrapper;
import com.unis.gameplatfrom.api.result.BaseCustomListResult;
import com.unis.gameplatfrom.base.BaseActivity;
import com.unis.gameplatfrom.cache.UserCenter;
import com.unis.gameplatfrom.model.GamesEntity;
import com.unis.gameplatfrom.model.LoginResult;
import com.unis.gameplatfrom.ui.widget.MetroViewBorderHandler;
import com.unis.gameplatfrom.ui.widget.MetroViewBorderImpl;
import com.unis.gameplatfrom.utils.DialogHelper;
import com.unis.gameplatfrom.utils.PackageUtil;
import com.unis.gameplatfrom.utils.TimeUtil;
import com.unis.gameplatfrom.utils.udateapk.DownloadAPk;
import com.unis.gameplatfrom.utils.udateapk.LinPermission;

import org.litepal.LitePal;

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
    RecyclerView mainRecycler;


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

    private MainAdapter mainAdapter;

    private List<GamesEntity> gamesEntities  = new ArrayList<>();

    private MainAdapter adapter;


    private String userName;
    private String userHead;

    private String game_account;


    private MediaController localMediaController;


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    private List<Integer> progressList = new ArrayList<>();


    @Override
    protected void initData() {

        adapter = new MainAdapter(mContext,gamesEntities);
        mainRecycler.setLayoutManager(new LinearLayoutManager(this));
        mainRecycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LitePal.getDatabase(); //创建数据表
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

                                    PackageUtil.startAppByPackageID(mContext,entity.getPackname());

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
                                PackageUtil.startAppByPackageID(mContext,entity.getPackname());

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


        userNameText.setText(userName);
        if(BuildConfig.DEBUG){

            versionText.setText("版本号： V"+PackageUtil.getVersionName(mContext));

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


        //设置有进度条可以拖动快进
        String url = "http://oa47.oss-cn-shenzhen.aliyuncs.com/2.mp4";
         localMediaController = new MediaController(this);
        //拿到MediaController
        videoDetails.setMediaController(localMediaController);
        //先加载地址
        videoDetails.setVideoPath(url);
        //localMediaController.show();

        firstImage.setImageBitmap(getVideoThumb(url));


        leftLayout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.game_toolbar));




        // finish();

    }


    @Override
    protected void onResume() {
        super.onResume();

        loadData();
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


                        if(result.getErr() == 0){

                            if (LinPermission.checkPermission(MainActivity.this, new int[]{7,8})) {


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

                                adapter.setNewData(gamesEntities);





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

                DownloadAPk.downApk(MainActivity.this, filepath, path, iconUrl,
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


        }



    }


    @OnClick({R.id.into_game,R.id.login_out,R.id.movie_play})
    public void onViewClicked(View view){

        switch (view.getId()){

            case R.id.into_game:

                if (LinPermission.checkPermission(MainActivity.this, new int[]{7,8})) {
                    Intent intent = new Intent(MainActivity.this,GamesActivity.class);
                    startActivity(intent);
                    finish();
                }

                break;

            case R.id.login_out:
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

                break;

            case R.id.movie_play:
                moviePlay.setVisibility(View.GONE);
                firstImage.setVisibility(View.GONE);
                videoDetails.start();
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



}
