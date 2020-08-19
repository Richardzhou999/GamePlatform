package com.unis.kotlin.ui

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.NetworkUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.squareup.picasso.Picasso
import com.unis.kotlin.R
import com.unis.kotlin.api.adapter.MainAdapter
import com.unis.kotlin.api.result.BaseCustomListResult
import com.unis.kotlin.base.BaseActivity
import com.unis.kotlin.cache.InnerReceiver
import com.unis.kotlin.cache.ItemNetConnectionReceiver
import com.unis.kotlin.cache.UserCenter
import com.unis.kotlin.constant.MainConstant
import com.unis.kotlin.entity.GamesEntity
import com.unis.kotlin.presenter.MainPresenter
import com.unis.kotlin.ui.widget.CircleTransform
import com.unis.kotlin.ui.widget.MetroViewBorderImpl
import com.unis.kotlin.utils.LinPermission
import com.unis.kotlin.utils.PackageUtil
import com.unis.kotlin.utils.TimeUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.litepal.LitePal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity<MainPresenter>(), MainConstant.View, View.OnClickListener{

    private var mainPresenter : MainPresenter? = null

    private var mainAdapter : MainAdapter? = null

    private var mMetroViewBorderImpl: MetroViewBorderImpl<*>? = null

    private

     var nowTime : Boolean? = false

    private val movieUrl = "http://oa47.oss-cn-shenzhen.aliyuncs.com/2.mp4"

    private val saveDownName : String? = null //正在下载的游戏名
    private val downGame : Boolean? = false //是否正在下载游戏

    private var gamesEntities : ArrayList<GamesEntity>? = null

    private var fristplay : Boolean? = false
    private var movie : Boolean = false

    private var refresh : Boolean? = false
    private var mConnectTag: Boolean = false            //防止刷新列表数据

    private var itemNetConnectionReceiver : ItemNetConnectionReceiver? = null

    private var mFilter:IntentFilter? = null
    private var localMediaController: MediaController? = null

    private val handler = object : Handler() {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val TimeFormat = SimpleDateFormat("HH:mm")// HH:mm:ss
            //获取当前时间
            val date1 = Date(System.currentTimeMillis())
            if (txt_now_time != null) {
                txt_now_time.text = TimeFormat.format(date1)
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun initData() {

        gamesEntities = ArrayList()
        mainAdapter = MainAdapter(mContext!!, gamesEntities!!)
        rv_main.setLayoutManager(LinearLayoutManager(this))
        rv_main.getItemAnimator().setAddDuration(0)
        rv_main.getItemAnimator().setChangeDuration(0)
        rv_main.getItemAnimator().setMoveDuration(0)
        rv_main.getItemAnimator().setRemoveDuration(0)
        (rv_main.getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations = false
        mMetroViewBorderImpl!!.attachTo(rv_main)
        var pool = rv_main.recycledViewPool
        pool.setMaxRecycledViews(0, 10)
        rv_main.recycledViewPool = pool
        rv_main.adapter = mainAdapter
        LitePal.getDatabase() //创建数据表


        itemNetConnectionReceiver = ItemNetConnectionReceiver()
        mFilter = IntentFilter()
        mFilter!!.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(itemNetConnectionReceiver, mFilter)

        mainAdapter!!.setOnItemClickListener{ adapter, view, position ->
            val entity = adapter.getItem(position) as GamesEntity?

            if (entity!!.v != 0) {

                if (LinPermission.checkPermission(this@MainActivity, intArrayOf(7, 8))) {

                    //                            if (mDownloadBinder != null) {
                    //                                long downloadId = mDownloadBinder.startDownload(APK_URL);
                    //                                startCheckProgress(downloadId);
                    //                            }


                    /**
                     * 情况1：记录不在，游戏在
                     * 情况2：记录不在，游戏不在
                     * 情况3：两者都在
                     */
                    /**
                     * 情况1：记录不在，游戏在
                     * 情况2：记录不在，游戏不在
                     * 情况3：两者都在
                     */
                    val gameId = entity.gameId
                    val entity1: GamesEntity?
                    if (gameId != 0) {
                        entity1 = LitePal.where("gameId=" + gameId).findFirst(GamesEntity::class.java)
                    } else {
                        entity1 = LitePal.where("gameId=" + gameId).findFirst(GamesEntity::class.java)
                    }

                    if (entity1 != null) {

                        //若游戏被删除，需清除游戏记录防止数据出错
                        if (PackageUtil.isAppByPackageID(mContext!!, entity.packname)) {



                            val number = entity.v


                            if (number > entity1.v) {

                                entity1.v = entity.v
                                entity.gameId = entity.id
                                UserCenter.get()!!.deleteGameFile(entity.packname!!)

                                val bool = entity1.save()
                                Log.e("xxxx", "更新保存$bool")
                                val entity2 = LitePal.where("gameId=" + gameId).findFirst(GamesEntity::class.java)
                                Log.e("xxxx", "" + entity2)
                            } else {

                                PackageUtil.startAppByPackageID(mContext!!, entity.packname!!,
                                        entity.id)

                            }


                        } else {

                            entity1.v = entity.v
                            entity1.gameId = entity.id
                            entity1.name = entity.name
                            entity1.p = entity.p
                            entity1.packname = entity.packname
                            entity1.icon = entity.icon
                            entity1.save()



                        }


                    } else {


                        if (PackageUtil.isAppByPackageID(mContext!!, entity.packname)) {


                            entity.gameId = entity.id
                            PackageUtil.startAppByPackageID(mContext!!, entity.packname!!, entity.id)
                            entity.save()
                        } else {

                            //第一次下载


                            entity.gameId = entity.id
                            entity.save()

                        }
                    }

                }
            }
        }

    }

    override fun initPresenter() {
        mainPresenter = MainPresenter(mContext!!)
        mainPresenter!!.attachView(this)
    }

    override fun initView(savedInstanceState: Bundle?) {

        var userName = UserCenter.get()!!.getUserName()
        var userHead = UserCenter.get()!!.getUserHead()
        user_name.text = userName
            Picasso.with(mContext).load(userHead)
                    .transform(CircleTransform())
                    //                .memoryPolicy(MemoryPolicy.NO_CACHE)
                    //                .networkPolicy(NetworkPolicy.NO_CACHE)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(user_image)

            val TimeFormat = SimpleDateFormat("HH:mm")// HH:mm:ss
            //获取当前时间
            val date1 = Date(System.currentTimeMillis())

            txt_month_week.text = TimeUtil.StringData()
            txt_now_time.text = TimeFormat.format(date1)

            mMetroViewBorderImpl = MetroViewBorderImpl<RecyclerView>(mContext, false)
            mMetroViewBorderImpl!!.setBackgroundResource(R.drawable.border_color)

            left_layout.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.game_toolbar))

            login_out.setOnFocusChangeListener { view, hasFocus ->

                if (hasFocus) {
                    login_out.setSelected(true)
                    login_out.setTextColor(ContextCompat.getColor(mContext!!, R.color.white))

                } else {

                    login_out.setSelected(false)
                    login_out.setTextColor(ContextCompat.getColor(mContext!!, R.color.out_login_txt))
            }


        }

        push_layout.setOnFocusChangeListener { view, hasFocus ->

            if(hasFocus){
                push_layout.setBackgroundResource(R.drawable.border_color)
            }else{
                push_layout.setBackgroundResource(R.color.transparent)
            }
        }

        movie_layout.setOnFocusChangeListener{ v, hasFocus ->
            if (hasFocus) {

                movie_layout.setBackgroundResource(R.drawable.border_color)
            } else {

                movie_layout.setBackgroundResource(android.R.color.transparent)
            }
        }


        TimeThread().start()

        movie_image.setOnCompletionListener{
            movie_image.setVideoPath(movieUrl)
            iv_first_frame.setImageBitmap(getVideoThumb(movieUrl))
            iv_first_frame.visibility = View.VISIBLE
            movie_play.visibility = View.VISIBLE

        }

        into_game.setOnClickListener(this)
        movie_layout.setOnClickListener(this)
        login_out.setOnClickListener(this)

    }

    override fun onResume() {
        super.onResume()

        //设置有进度条可以拖动快进
        localMediaController = MediaController(this)
        //拿到MediaController
        movie_image.setMediaController(localMediaController)

        if (NetworkUtils.isConnected()) {

            //需要网络的情况下先加载地址
            movie_image.setVideoPath(movieUrl)
            //localMediaController.show();
            iv_first_frame.setImageBitmap(getVideoThumb(movieUrl))


            movie_image.setFocusable(false)
            movie_image.setFocusableInTouchMode(false)
            loadData()

            push_layout.setFocusable(false)
            push_layout.setFocusableInTouchMode(false)
            login_out.setFocusable(false)
            login_out.setFocusableInTouchMode(false)
            movie_layout.setFocusable(false)
            movie_layout.setFocusableInTouchMode(false)
            rv_main.setFocusable(true)
            rv_main.setFocusableInTouchMode(true)

            if (!refresh!!) {
                rv_main.requestFocus()
            }

        } else {

            Toast.makeText(mContext, "当前网络异常,请检查网络.网络无异常将自动刷新列表", Toast.LENGTH_LONG).show()
            mConnectTag = true
        }
    }

    private fun loadData() {
        //game_account = UserCenter.getInstance().getGame_account()

        if (NetworkUtils.isConnected()) {
            mainPresenter!!.getGameList(UserCenter.get()!!.getToken())
        }
    }



    override fun onSuccess(result: BaseCustomListResult<GamesEntity>) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            into_game.focusable = View.FOCUSABLE
            push_layout.focusable = View.FOCUSABLE
            login_out.focusable = View.FOCUSABLE
            movie_layout.focusable = View.FOCUSABLE
        }

        var entity1: GamesEntity? = null

        if (gamesEntities!!.size != 0) {
            gamesEntities!!.clear()
        }

        if (result.err == 0 && result.glist!!.isNotEmpty()) {

            if (LinPermission.checkPermission(this@MainActivity, intArrayOf(7, 8))) {

                if (result.glist!!.size < 3) {

                    for (entity in result.glist!!.iterator()) {



                        val gameId = entity.gameId

                        if (gameId != 0) {
                            entity1 = LitePal.where("gameId=" + gameId).findFirst(GamesEntity::class.java)
                        } else {
                            entity1 = LitePal.where("gameId=" + gameId).findFirst(GamesEntity::class.java)
                        }

                        if (entity1 != null) {
                            // entity.setV(entity.getV()+1);

                            if (PackageUtil.isAppByPackageID(mContext!!, entity.packname)) {


                                if (entity.v > entity1.v) {

                                    entity.isNewGame = true
                                    entity.isInstallGame = true
                                    entity.isLocal = true
                                    entity.newVersion  = entity1.v
                                    gamesEntities!!.add(entity)

                                } else {

                                    entity.isLocal = true
                                    entity.isInstallGame = true
                                    gamesEntities!!.add(entity)

                                }


                            } else {

                                entity1.save()
                                entity1.delete()

                                if (PackageUtil.isAppByLocal(entity.p!!)) {

                                    entity.isNewGame  = false
                                    entity.isInstallGame = false
                                    entity.isLocal = true
                                    gamesEntities!!.add(entity)

                                } else {

                                    entity.isNewGame = false
                                    entity.isInstallGame = false
                                    entity.isLocal = false
                                    gamesEntities!!.add(entity)

                                }
                            }

                        } else {

                            if (PackageUtil.isAppByPackageID(mContext!!, entity.packname)) {


                                entity.isLocal = true
                                entity.isNewGame = false
                                entity.isInstallGame = true
                                gamesEntities!!.add(entity)

                            } else {


                                if (PackageUtil.isAppByLocal(entity.p!!)) {

                                    entity.isNewGame = false
                                    entity.isInstallGame = false
                                    entity.isLocal = true
                                    gamesEntities!!.add(entity)

                                } else {

                                    entity.isNewGame = false
                                    entity.isInstallGame = false
                                    entity.isLocal = false
                                    gamesEntities!!.add(entity)

                                }


                            }

                        }


                    }


                } else {

                    for (entity in result.glist!!.iterator()) {




                        val gameid = entity.gameId

                        if (gameid != 0) {
                            entity1 = LitePal.where("gameId=" + gameid).findFirst(GamesEntity::class.java)
                        } else {
                            entity1 = LitePal.where("gameId=" + gameid).findFirst(GamesEntity::class.java)
                        }

                        if (entity1 != null) {
                            // entity.setV(entity.getV()+1);

                            if (PackageUtil.isAppByPackageID(mContext!!, entity.packname)) {


                                if (entity.v > entity1.v) {

                                    entity.isNewGame = true
                                    entity.isInstallGame = true
                                    entity.isLocal = true
                                    entity.newVersion = entity1.v
                                    gamesEntities!!.add(entity)

                                } else {

                                    entity.isLocal = true
                                    entity.isInstallGame = true
                                    gamesEntities!!.add(entity)

                                }


                            } else {

                                entity1.save()
                                entity1.delete()

                                if (PackageUtil.isAppByLocal(entity.p!!)) {

                                    entity.isNewGame = false
                                    entity.isInstallGame = false
                                    entity.isLocal  = true
                                    gamesEntities!!.add(entity)

                                } else {

                                    entity.isNewGame = false
                                    entity.isInstallGame = false
                                    entity.isLocal = false
                                    gamesEntities!!.add(entity)

                                }
                            }

                        } else {

                            if (PackageUtil.isAppByPackageID(mContext!!, entity.packname)) {


                                entity.isLocal = true
                                entity.isNewGame = false
                                entity.isInstallGame = true
                                gamesEntities!!.add(entity)

                            } else {


                                if (PackageUtil.isAppByLocal(entity.p!!)) {

                                    entity.isNewGame = false
                                    entity.isInstallGame = false
                                    entity.isLocal = true
                                    gamesEntities!!.add(entity)

                                } else {

                                    entity.isNewGame = false
                                    entity.isInstallGame = false
                                    entity.isLocal = false
                                    gamesEntities!!.add(entity)

                                }
                            }
                        }
                    }
                }


                if (!refresh!!) {
                    refresh = true
                    mainAdapter!!.setNewData(gamesEntities)
                } else {



                }
                //mainRecycler.smoothScrollToPosition(1);


            } else {

                //                            adapter.setNewData(result.getData());
                //                            rightAdapter.setNewData(result.getData());
                //                            mRefreshLayout.finishRefresh();

                //finish();
                //                        adapter.addData(result.getData());

            }

        } else {

            //Toast.makeText(mContext, result.getMsg(), Toast.LENGTH_LONG).show();

            //finish();
            //startActivity(new Intent(mContext, LoginActivity.class));

        }


    }

    override fun onClick(view: View?) {
        when(view?.id){
             R.id.into_game -> {

                 if (!downGame!!) {
                     nowTime = false
                     val intent = Intent(this@MainActivity, GamesActivity::class.java)
                     startActivity(intent)
                 } else {
                     Toast.makeText(mContext, saveDownName + "  正在下载, 请勿退出", Toast.LENGTH_SHORT).show()
                 }
             }

            R.id.movie_layout -> {
                if (!fristplay!!) {

                    movie_play.visibility = View.GONE
                    iv_first_frame.visibility = View.GONE
                    movie_image.start()
                    fristplay = true

                } else {

                    if (!movie) {

                        movie_play.visibility = View.VISIBLE
                        movie_image.pause()
                        movie = true


                    } else {

                        movie_play.visibility = View.GONE
                        movie_image.start()
                        movie = false


                    }


                }


            }

            R.id.login_out -> {

                if (!downGame!!) {
                    nowTime = false
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    UserCenter.get()!!.delete_uuid()
                    finish()

                } else {

                    Toast.makeText(mContext, "$saveDownName  正在下载, 请勿退出", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }


    /**
     * 开启一个线程，每个一秒钟更新时间
     */
    inner class TimeThread : Thread() {


        //重写run方法
        override fun run() {
            super.run()

            while (nowTime!!) {
                try {
                    //每隔一秒 发送一次消息
                    Thread.sleep(100)
                    val msg = handler.obtainMessage()
                    //发送
                    handler.sendMessage(msg)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }

        }
    }

    /**
     * 获取视频文件截图
     *
     * @param url 视频文件的路径
     * @return Bitmap 返回获取的Bitmap
     */
    fun getVideoThumb(url: String): Bitmap {

        val media = MediaMetadataRetriever()

        media.setDataSource(url, HashMap())

        return media.frameAtTime

    }


    override fun hideLoading() {
        super<MainConstant.View>.hideLoading()
    }

    override fun showLoading() {
        super<MainConstant.View>.showLoading()
    }

    override fun onError(throwable: Throwable) {
        super<MainConstant.View>.onError(throwable)
    }



}
