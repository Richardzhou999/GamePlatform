package com.unis.kotlin.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.util.Log
import android.view.View
import android.widget.Toast
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.unis.kotlin.R
import com.unis.kotlin.api.adapter.GameAdapter
import com.unis.kotlin.api.result.BaseCustomListResult
import com.unis.kotlin.base.BaseActivity
import com.unis.kotlin.cache.InnerReceiver
import com.unis.kotlin.cache.UserCenter
import com.unis.kotlin.constant.GameConstant
import com.unis.kotlin.entity.GamesEntity
import com.unis.kotlin.presenter.GamePresenter
import com.unis.kotlin.ui.widget.MetroViewBorderImpl
import com.unis.kotlin.utils.LinPermission
import com.unis.kotlin.utils.PackageUtil
import kotlinx.android.synthetic.main.activity_game.*
import org.litepal.LitePal

class GamesActivity : BaseActivity<GamePresenter>(),GameConstant.View , View.OnClickListener{



    private var gamePresenter : GamePresenter? = null

    private var gameList : ArrayList<GamesEntity>? = null

    private var adapter : GameAdapter? = null

    private var gameaccount : String? = null

    private var mMetroViewBorderImpl: MetroViewBorderImpl<*>? = null



    private var innerReceiver : InnerReceiver? = null
    private var itemNetConnectionReceiver : ItemNetConnectionReceiver? = null

    private var mFilter : IntentFilter? = null

    var mConnectTag : Boolean? = false
    var refresh : Boolean? = false

    override fun getLayout(): Int {
       return R.layout.activity_game
    }

    override fun initData() {

        gameList = ArrayList()
        adapter = GameAdapter(mContext!!,R.layout.item_games,gameList!!)
        var linearLayoutManager = LinearLayoutManager(mContext)
        rv_games.layoutManager = linearLayoutManager
        rv_games.getItemAnimator().setAddDuration(0)
        rv_games.getItemAnimator().setChangeDuration(0)
        rv_games.getItemAnimator().setMoveDuration(0)
        rv_games.getItemAnimator().setRemoveDuration(0)
        (rv_games.getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations = false



        mMetroViewBorderImpl!!.attachTo(rv_games)
        rv_games.adapter = adapter

        val filter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        registerReceiver(innerReceiver, filter)

        itemNetConnectionReceiver = ItemNetConnectionReceiver()
        mFilter = IntentFilter()
        mFilter!!.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(itemNetConnectionReceiver, mFilter)
        LitePal.getDatabase() //创建数据表

        adapter!!.setOnItemClickListener{ adapter, view, position ->

            val entity = adapter.getItem(position) as GamesEntity?

            if (entity!!.v != 0) {

                if (LinPermission.checkPermission(this@GamesActivity, intArrayOf(7, 8))) run {

                    //                            if (mDownloadBinder != null) {
                    //                                long downloadId = mDownloadBinder.startDownload(APK_URL);
                    //                                startCheckProgress(downloadId);
                    //                            }


                    /**
                     * 情况1：记录不在，游戏在
                     * 情况2：记录不在，游戏不在
                     * 情况3：两者都在
                     */
                    entity.gameId = entity.id
                    val entity1 = LitePal.where("gameId=" + entity.gameId).findFirst(GamesEntity::class.java)
                    if (entity1 != null) {

                        //若游戏被删除，需清除游戏记录防止数据出错
                        if (PackageUtil.isAppByPackageID(mContext!!, entity.packname)) {

                            val number = entity.v
                            if (number > entity1.v) {


                                entity1.v = entity.v
                                entity.gameId = entity.id
                                UserCenter.get()!!.deleteGameFile(entity.packname!!)

                                entity1.save()

                            } else {


                                entity.isDownGame = false
                                entity.gameId = entity.id
                                PackageUtil.startAppByPackageID(mContext!!,
                                        entity.packname!!, entity.id)

                            }


                        } else {

                            entity1.v = entity.v
                            entity.gameId = entity.id
                            entity1.name = entity.name
                            entity1.p = entity.p
                            entity1.packname = entity.packname
                            entity1.icon = entity.icon
                            entity1.save()
                            //entity.setDownGame(true);


                        }


                    } else {


                        if (PackageUtil.isAppByPackageID(mContext!!, entity.packname)) {


                            entity.gameId  = entity.id
                            entity.isDownGame = false
                            PackageUtil.startAppByPackageID(mContext!!,
                                    entity.packname!!, entity.id)
                            entity.save()
                        } else {

                            //第一次下载


                            entity.gameId  = entity.id
                            //entity.setDownGame(true);


                        }

                    }


                }
            }

        }

    }

    override fun initPresenter() {
        gamePresenter = GamePresenter(this)
        gamePresenter!!.attachView(this)
    }

    override fun initView(savedInstanceState: Bundle?) {

        back.setOnClickListener(this)

        gameaccount = UserCenter.get()!!.getGame_account()

        mMetroViewBorderImpl = MetroViewBorderImpl<RecyclerView>(mContext,false)
        mMetroViewBorderImpl!!.setBackgroundResource(R.drawable.border_color)

        back.setOnFocusChangeListener { view, hasFocus ->

            if (hasFocus) {
                back.setSelected(true)
            } else {
                back.setSelected(false)
            }

        }

    }


    override fun onResume() {
        super.onResume()

        if (NetworkUtils.isConnected()) {
            back.setFocusable(false)
            back.setFocusableInTouchMode(false)
            loadData()
            //        gameLayout.setFocusable(false);
            //        gameLayout.setFocusableInTouchMode(false);

            rv_games.setFocusable(true)
            rv_games.setFocusableInTouchMode(true)
            if (!refresh!!)
                rv_games.requestFocus()
        } else {

            ToastUtils.showShort("当前网络异常,请检查网络.网络无异常将自动刷新列表")

        }


    }

    /**
     * 数据加载点
     */
    private fun loadData() {

        if(NetworkUtils.isConnected()){
            gamePresenter!!.getGameList(UserCenter.get()!!.getToken())
        }

    }

    override fun onClick(view: View?) {

        finish()
    }

    override fun onSuccess(result: BaseCustomListResult<GamesEntity>) {


        if (gameList!!.size != 0) {
            gameList!!.clear()
        }

        back.setFocusable(true)
        back.isFocusableInTouchMode = false

        if (result.err == 0) {

            for (entity in result.glist!!) {

                entity.gameId = entity.id
                var oldEntity = LitePal.where("gameId=" + entity.gameId).findFirst(GamesEntity::class.java)

                if (oldEntity != null) {
                    // entity.setV(entity.getV()+1);

                    if (PackageUtil.isAppByPackageID(mContext!!, entity.packname)) {


                        if (entity.v > oldEntity.v) {

                            entity.isNewGame = true
                            entity.isInstallGame = true
                            entity.isLocal = true
                            entity.newVersion = oldEntity.v
                            gameList!!.add(entity)

                        } else {

                            entity.isNewGame = false
                            entity.isLocal =true
                            entity.isInstallGame = true
                            gameList!!.add(entity)

                        }


                    } else {

                        //                            entity1.save();
                        //                            entity1.delete();

                        if (PackageUtil.isAppByLocal(entity.p!!)) {

                            entity.isNewGame = false
                            entity.isInstallGame = false
                            entity.isLocal = true
                            gameList!!.add(entity)

                        } else {

                            entity.isNewGame = false
                            entity.isInstallGame = false
                            entity.isLocal = false
                            gameList!!.add(entity)

                        }
                    }

                } else {

                    val appby = PackageUtil.isAppByPackageID(mContext!!, entity.packname)

                    if (appby) {

                        entity.isLocal = true
                        entity.isNewGame = false
                        entity.isInstallGame = true
                        gameList!!.add(entity)

                    } else {


                        if (PackageUtil.isAppByLocal(entity.p!!)) {

                            entity.isNewGame = false
                            entity.isInstallGame = false
                            entity.isLocal = true
                            gameList!!.add(entity)

                        } else {

                            entity.isNewGame = false
                            entity.isInstallGame = false
                            entity.isLocal = false
                            gameList!!.add(entity)

                        }
                    }

                }

            }

            if (!refresh!!) run {

                refresh = true
                adapter!!.setNewData(gameList)
                //getGames.clear();
                //adapter.notifyItemRangeRemoved(0,size);
                //adapter.notifyItemRangeChanged(0,getGames.size());
            }


        }else{

            Toast.makeText(mContext, result.msg, Toast.LENGTH_SHORT).show()
            startActivity(Intent(mContext, LoginActivity::class.java))
            finish()

        }


    }




    inner class ItemNetConnectionReceiver : BroadcastReceiver(){

        override fun onReceive(context: Context?, p1: Intent?) {

            val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET)
            val wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val networkInfos = connectivityManager.allNetworkInfo
                Log.e("xxx", "xx$networkInfos")
            }


            if (mobNetInfo != null && wifiNetInfo != null) {

                if (!mobNetInfo.isConnected && !wifiNetInfo.isConnected) {
                    //网络连接已断开
                    //            for (DownloadTask downloadTask : downloadTasks) {
                    //                downloadTask.pauseDownload();//暂停所有下载任务
                    //            }
                    Toast.makeText(context, "游戏大厅网络异常,请检查网络", Toast.LENGTH_SHORT).show()
                    mConnectTag = true
                    //                    if(mDownloadMgr != null ){
                    //
                    //                        mDownloadMgr.pauseAllTask();//暂停所有下载任务
                    //                    }

                } else {
                    //网络连接已连接

                    if (mConnectTag!!) {
                        mConnectTag = false
                        refresh = false
                        loadData()
                    }

                    //                    if(mDownloadMgr != null ){
                    //                        mDownloadMgr.startAllTask();//继续所有下载任务
                    //                    }


                }

            }


        }


    }




    override fun hideLoading() {}

    override fun onError(throwable: Throwable) {}

    override fun showLoading() {}



}
