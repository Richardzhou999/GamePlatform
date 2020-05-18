package com.unis.gameplatfrom.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @author wulei
 * @date 2017/8/31
 */

public class IntentUtil {

    public static void startWebIntent(Context ctx, String url) {
        try {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            Uri content_url = Uri.parse(url);
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction("android.intent.action.VIEW");
            intent.setData(content_url);
            ctx.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ctx, "无法打开网址:" + url, Toast.LENGTH_SHORT).show();
        }
    }
}
