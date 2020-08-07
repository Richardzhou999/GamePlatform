package com.unis.gameplatfrom.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;
import com.blankj.utilcode.util.NetworkUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.adapter.GameListAdapter;
import com.unis.gameplatfrom.adapter.GamesAdapter;
import com.unis.gameplatfrom.api.HUDLoadDataSubscriber;
import com.unis.gameplatfrom.api.PublicApiInterface;
import com.unis.gameplatfrom.api.RetrofitWrapper;
import com.unis.gameplatfrom.api.result.BaseCustomListResult;
import com.unis.gameplatfrom.base.BaseActivity;
import com.unis.gameplatfrom.cache.UserCenter;
import com.unis.gameplatfrom.constant.GameConstant;
import com.unis.gameplatfrom.entity.GamesEntity;
import com.unis.gameplatfrom.entity.GamesListEntity;
import com.unis.gameplatfrom.presenter.GamePresenter;
import com.unis.gameplatfrom.ui.widget.MetroViewBorderImpl;
import com.unis.gameplatfrom.utils.DialogHelper;
import com.unis.gameplatfrom.utils.PackageUtil;
import com.unis.gameplatfrom.utils.udateapk.DownLoadApkService;
import com.unis.gameplatfrom.utils.udateapk.LinNotify;
import com.unis.gameplatfrom.utils.udateapk.LinPermission;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yong
 * on 2018/6/20.
 */


public class GamesListActivity extends BaseActivity<GamePresenter> implements GameConstant.View {

    @BindView(R.id.layout_game)
    LinearLayout gameLayout;


    @BindView(R.id.toolbar_left)
    TextView toolbarLeft;


    @BindView(R.id.rv_games)
    RecyclerView gamesRecycler;

//    @BindView(R.id.refresh_layout)
//    SmartRefreshLayout mRefreshLayout;


    public static int mProgress;

    private GameListAdapter mAdapter;


    private List<GamesListEntity> getGames = new ArrayList<>();


    private String game_account;

    private int page = 1;

    public boolean cancle;

    private View FooterView;

    private MetroViewBorderImpl mMetroViewBorderImpl;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


        }
    };

    private String TAG = "GamesListActivity";

    private List<Integer> progressList = new ArrayList<>();


    private GamePresenter gamePresenter;


    @Override
    protected int getLayout() {
        return R.layout.activity_games;
    }


    @Override
    protected void initData() {


        FooterView = getLayoutInflater().inflate(R.layout.item_footer, null);

        //左边
        getGames = new ArrayList<>();
        mAdapter = new GameListAdapter(mContext, getGames);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        gamesRecycler.setLayoutManager(layoutManager);
        gamesRecycler.getItemAnimator().setAddDuration(0);
        gamesRecycler.getItemAnimator().setChangeDuration(0);
        gamesRecycler.getItemAnimator().setMoveDuration(0);
        gamesRecycler.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) gamesRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        mMetroViewBorderImpl.attachTo(gamesRecycler);
        gamesRecycler.setAdapter(mAdapter);


//        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//
//                loadData();
//
//            }
//        });


//        mRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//
//                loadData();
//
//            }
//
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//               // refreshlayout.setRefreshFooter()
//                FooterView.setVisibility(View.VISIBLE);
//                loadData();
//
//            }
//        });


    }

    @Override
    protected void initPresenter() {
        gamePresenter = new GamePresenter(mContext);
        gamePresenter.attachView(this);
    }


    @OnClick({R.id.back})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.back:
                startActivity(new Intent(GamesListActivity.this, MainActivity.class));
                finish();

                break;
            default:
                break;


        }


    }


    @Override
    protected void initView(Bundle savedInstanceState) {


        //创建通道
        LinNotify.setNotificationChannel(GamesListActivity.this);
        StringBuilder builder = new StringBuilder();
        builder.append("厅大戏游");
        StringBuilder builder1 = new StringBuilder();
        builder1.append("厅大戏游");


        LitePal.getDatabase(); //创建数据表

        game_account = UserCenter.getInstance().getGame_account();

        gameLayout.getBackground().setAlpha(30);

        //Aria.download(this).register();

        mMetroViewBorderImpl = new MetroViewBorderImpl(this, false);
        mMetroViewBorderImpl.setBackgroundResource(R.drawable.border_color);

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

        if (getGames.size() != 0) {
            getGames.clear();
        }

        if (NetworkUtils.isConnected()) {
            RetrofitWrapper.getInstance().create(PublicApiInterface.class)
                    .getGameList1(UserCenter.getInstance().getToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    //.compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new HUDLoadDataSubscriber<BaseCustomListResult<GamesListEntity>>(mContext) {
                        @Override
                        public void onNext(BaseCustomListResult<GamesListEntity> result) {

                            if (result.getErr() == 0) {

                                if (LinPermission.checkPermission(GamesListActivity.this, new int[]{7, 8})) {


                                    for (GamesListEntity entity : result.getData()) {


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

                                            if (PackageUtil.isAppByPackageID(mContext, entity.getPackname())) {


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
                                }

                            mAdapter.notifyDataSetChanged();
//                            rightAdapter.setNewData(result.getData());
//                            mRefreshLayout.finishRefresh();

                                    //finish();
//                        adapter.addData(result.getData());



                            } else {

                                Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_SHORT).show();
//            if(mRefreshLayout.isRefreshing()){
//                mRefreshLayout.finishRefresh();
//            }
//            if(mRefreshLayout.isLoading()){
//                mRefreshLayout.finishLoadmore();
//            }
                            }


                        }
                    });
        }


    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onSuccess(BaseCustomListResult<GamesEntity> result) {

//        if(result.getErr() == 0){
//
//            if (LinPermission.checkPermission(GamesListActivity.this, new int[]{7,8})) {
//
//
//                for(GamesEntity entity : result.getData()){
//
//
//                    GamesEntity entity1 = LitePal.where("id="+entity.getId()).findFirst(GamesEntity.class);
//                    if( entity1 != null ){
//                        // entity.setV(entity.getV()+1);
//
//                        if(PackageUtil.isAppByPackageID(mContext,entity.getPackname())){
//
//
//
//                            if(entity.getV() > entity1.getV()){
//
//                                entity.setNewGame(true);
//                                entity.setInstallGame(true);
//                                entity.setLocal(true);
//                                getGames.add(entity);
//
//                            }else {
//
//                                entity.setLocal(true);
//                                entity.setInstallGame(true);
//                                getGames.add(entity);
//
//                            }
//
//
//                        }else {
//
//                            entity1.save();
//                            entity1.delete();
//
//                            if(PackageUtil.isAppByLocal(entity.getP())){
//
//                                entity.setNewGame(false);
//                                entity.setInstallGame(false);
//                                entity.setLocal(true);
//                                getGames.add(entity);
//
//                            }else {
//
//                                entity.setNewGame(false);
//                                entity.setInstallGame(false);
//                                entity.setLocal(false);
//                                getGames.add(entity);
//
//                            }
//                        }
//
//                    }else {
//
//                        if(PackageUtil.isAppByPackageID(mContext,entity.getPackname())){
//
//
//                            entity.setLocal(true);
//                            entity.setNewGame(false);
//                            entity.setInstallGame(true);
//                            getGames.add(entity);
//
//                        }else {
//
//
//                            if(PackageUtil.isAppByLocal(entity.getP())){
//
//                                entity.setNewGame(false);
//                                entity.setInstallGame(false);
//                                entity.setLocal(true);
//                                getGames.add(entity);
//
//                            }else {
//
//                                entity.setNewGame(false);
//                                entity.setInstallGame(false);
//                                entity.setLocal(false);
//                                getGames.add(entity);
//
//                            }
//
//
//
//                        }
//
//                    }
//
//
//
//                }
//
////                                mAdapter = new GameListAdapter(mContext,GamesListActivity.this,getGames);
////                                gamesRv.setLayoutManager(new LinearLayoutManager(mContext));
////                                gamesRv.setAdapter(mAdapter);
//
////                if(mRefreshLayout.isRefreshing()){
////                    mRefreshLayout.finishRefresh();
////                }
////                if(mRefreshLayout.isLoading()){
////                    mRefreshLayout.finishLoadmore();
////                    FooterView.setVisibility(View.GONE);
////                }
//
//
//
//            }
//            else {
//
////                            adapter.setNewData(result.getData());
////                            rightAdapter.setNewData(result.getData());
////                            mRefreshLayout.finishRefresh();
//
//                //finish();
////                        adapter.addData(result.getData());
//
//            }
//
//        }else {
//
//            Toast.makeText(mContext,result.getMsg(),Toast.LENGTH_SHORT).show();
////            if(mRefreshLayout.isRefreshing()){
////                mRefreshLayout.finishRefresh();
////            }
////            if(mRefreshLayout.isLoading()){
////                mRefreshLayout.finishLoadmore();
////            }
//        }


    }


    //============================================== 下载方法 =======================================

    @Download.onPre
    void onPre(DownloadTask task) {
        mAdapter.updateState((GamesListEntity) task.getEntity());
        Log.d(TAG, task.getTaskName() + ", " + task.getState());
    }

    @Download.onWait
    void onWait(DownloadTask task) {
        mAdapter.updateState((GamesListEntity) task.getEntity());
    }

    @Download.onTaskStart
    void taskStart(DownloadTask task) {
        Log.d(TAG, task.getTaskName() + ", " + task.getState());
        mAdapter.updateState((GamesListEntity) task.getEntity());
    }

    @Download.onTaskResume
    void taskResume(DownloadTask task) {
        Log.d(TAG, task.getTaskName() + ", " + task.getState());
        mAdapter.updateState((GamesListEntity) task.getEntity());
    }

    @Download.onTaskStop
    void taskStop(DownloadTask task) {
        mAdapter.updateState((GamesListEntity) task.getEntity());
    }

    @Download.onTaskCancel
    void taskCancel(DownloadTask task) {
        mAdapter.updateState((GamesListEntity) task.getEntity());
    }

    @Download.onTaskFail
    void taskFail(DownloadTask task) {
        if (task == null || task.getEntity() == null) {
            return;
        }
        mAdapter.updateState((GamesListEntity) task.getEntity());
    }

    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {
        mAdapter.updateState((GamesListEntity) task.getEntity());
    }

    @Download.onTaskRunning
    void taskRunning(DownloadTask task) {
        mAdapter.setProgress((GamesListEntity) task.getEntity());
    }


}
