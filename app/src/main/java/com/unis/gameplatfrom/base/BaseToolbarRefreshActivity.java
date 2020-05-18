package com.unis.gameplatfrom.base;

import android.os.Bundle;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;


import butterknife.BindView;
import com.unis.gameplatfrom.Constant;
import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.myinterface.IRefresh;

/**
 * Created by wulei on 16/9/13.
 */

@SuppressWarnings("DefaultFileTemplate")
public abstract class BaseToolbarRefreshActivity extends BaseToolBarActivity implements IRefresh {
    public static final int OPERATE_NORMAL = 1;
    public static final int OPERATE_REFRESH = 2;
    public static final int OPERATE_LOADMORE = 3;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private int page = 1;
    private int pageSize = Constant.PAGE_SIZE;

    private int operateState = OPERATE_NORMAL;

    public int getPageSize() {
        return pageSize;
    }

    public int getPage() {
        return page;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initRefreshLayout();
    }

    protected boolean hasRefresh() {
        return true;
    }

    protected boolean hasLoadMore() {
        return true;
    }

    private void initRefreshLayout() {

        mRefreshLayout.setEnableRefresh(hasRefresh());
        mRefreshLayout.setEnableLoadmore(hasLoadMore());

        mRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));

        mRefreshLayout.setOnRefreshListener(refreshlayout -> {
            operateState = OPERATE_REFRESH;
            page = 1;
            BaseToolbarRefreshActivity.this.onRefresh((View) refreshlayout);
        });

        mRefreshLayout.setOnLoadmoreListener(refreshlayout -> {
            operateState = OPERATE_LOADMORE;
            page++;
            BaseToolbarRefreshActivity.this.onLoadMore((View) refreshlayout);
        });
    }

    /**
     * 是否刷新操作
     *
     * @return
     */
    @Override
    public boolean isRefresh() {
        return operateState == OPERATE_NORMAL || operateState == OPERATE_REFRESH;
    }

    // 通过代码方式控制进入正在刷新状态。应用场景：某些应用在activity的onStart方法中调用，自动进入正在刷新状态获取最新数据
    @Override
    public void beginRefreshing() {
        operateState = OPERATE_REFRESH;
        page = 1;
        mRefreshLayout.autoRefresh();
    }

    // 通过代码方式控制进入加载更多状态
    @Override
    public void beginLoadingMore() {
        operateState = OPERATE_LOADMORE;
        page++;
        mRefreshLayout.autoLoadmore();
    }

    @Override
    public abstract void onRefresh(View refreshLayout);

    @Override
    public abstract void onLoadMore(View refreshLayout);

    @Override
    public void endRefreshOrLoadMore() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh(1000);
            mRefreshLayout.finishLoadmore(1000);
        }
    }

    @Override
    public void onError(int code, String errmsg) {
        showMessageDialog(errmsg);
    }
}
