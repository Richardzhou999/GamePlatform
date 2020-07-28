package com.unis.gameplatfrom.api;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Created by wulei on 2016/9/24.
 */

@SuppressWarnings("DefaultFileTemplate")
public abstract class HUDLoadDataSubscriber<T> extends SimpleDataSubscriber<T> {
    private KProgressHUD kProgressHUD;

    public HUDLoadDataSubscriber(Context ctx) {
        this(ctx, null);
    }

    public HUDLoadDataSubscriber(Context ctx, String msg) {
        kProgressHUD = KProgressHUD.create(ctx)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        if (!TextUtils.isEmpty(msg)) {
            kProgressHUD.setLabel(msg);
        }
        kProgressHUD.show();
    }

    @Override
    public void onComplete() {
        kProgressHUD.dismiss();
    }

    @Override
    public void exception(int code, String message) {
        kProgressHUD.setLabel(message);
        new Handler().postDelayed(() -> kProgressHUD.dismiss(), 1500);

    }

    public void dismissHud() {
        kProgressHUD.dismiss();
    }
}
