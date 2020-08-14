package com.unis.kotlin.api

import android.content.Context
import android.os.Handler
import android.text.TextUtils
import com.kaopiz.kprogresshud.KProgressHUD
import org.greenrobot.eventbus.Subscribe

open class HUDLoadDataSubscriber<T> constructor() : SimpleDataSubscriber<T>(){

    private var kProgressHUD: KProgressHUD? = null


    constructor(ctx: Context): this(ctx,null)

    constructor(ctx: Context, msg: String?) : this() {
        kProgressHUD = KProgressHUD.create(ctx)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)

        if (!TextUtils.isEmpty(msg)) {
            kProgressHUD!!.setLabel(msg)
        }
        kProgressHUD!!.show()
    }

    override fun onComplete() {
        kProgressHUD!!.dismiss()
    }

    override fun exception(code: Int, message: String) {
        kProgressHUD!!.setLabel(message)
        Handler().postDelayed({ kProgressHUD!!.dismiss() }, 1500)

    }

    fun dismissHud() {
        kProgressHUD!!.dismiss()
    }

}