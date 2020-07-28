package com.unis.gameplatfrom.myinterface;

import android.view.View;

/**
 * Created by wulei on 2016/12/16.
 */

@SuppressWarnings("DefaultFileTemplate")
public interface IRefresh {
    boolean isRefresh();

    void beginRefreshing();

    void beginLoadingMore();

    void onRefresh(View refreshLayout);

    void onLoadMore(View refreshLayout);

    void endRefreshOrLoadMore();

    void onError(int code, String errmsg);
}
