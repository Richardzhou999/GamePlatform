package com.unis.gameplatfrom.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.TextUtils;

import com.kaopiz.kprogresshud.KProgressHUD;



/**
 * 对话框帮助类
 * Created by wulei on 2016/11/23.
 */

public class DialogHelper {
    private static KProgressHUD kProgressHUD;

    public static void showAlertDialog(Context context, String message,
                                       String confirmText, String cancelText, DialogInterface.OnClickListener confirmClickListener,
                                       DialogInterface.OnClickListener cancelClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        if (!TextUtils.isEmpty(confirmText)) {
            builder.setConfirmButton(confirmText, confirmClickListener);
        }
        if (!TextUtils.isEmpty(cancelText)) {
            builder.setCancelButton(cancelText, cancelClickListener);
        }
        builder.create().show();
    }

    public static void showHud(Context ctx) {
        showHud(ctx, null);
    }
    public static void showHud(Context ctx, String msg) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(ctx)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);
        }

        if (!TextUtils.isEmpty(msg)) {
            kProgressHUD.setLabel(msg);
        }
        kProgressHUD.show();
    }

    public static void showHudMessage(Context ctx, String message) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(ctx)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);
        }
        kProgressHUD.setLabel(message);
        kProgressHUD.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                kProgressHUD.dismiss();
            }
        }, 1500);

    }

    public static void dismissHud() {
        if (kProgressHUD != null) {
            kProgressHUD.dismiss();
        }
    }

}
