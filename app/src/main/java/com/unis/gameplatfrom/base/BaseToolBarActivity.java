package com.unis.gameplatfrom.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SizeUtils;


import butterknife.BindView;
import com.unis.gameplatfrom.R;

/**
 * 包含Toolbar的Activit基类
 * <p>
 * Created by wulei on 16/6/3.
 */

public abstract class BaseToolBarActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_content)
    ViewGroup toolbarContent;
    @BindView(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.ll_toolbar_menu)
    LinearLayout llToolbarMenuView;
    @BindView(R.id.toolbar_bottom_line)
    ImageView ivBottomLine;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        onCreateCustomToolBar(toolbar);
        getSupportActionBar().setElevation(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && needFitsSystemWindows()) {
            toolbarContent.setPadding(0, BarUtils.getStatusBarHeight(this), 0, 0);
        }
    }

    public void onCreateCustomToolBar(Toolbar toolbar) {
        toolbar.setContentInsetsRelative(0, 0);
        String title = this.getTitle().toString();
        tvToolbarTitle.setText(title);
        if (ivToolbarBack != null) {
            ivToolbarBack.setOnClickListener(view -> onBackClick(view));
        }
    }

    protected void hideBackButton() {
        ivToolbarBack.setVisibility(View.GONE);
    }

    protected void hideToolbarBottomLine() {
        ivBottomLine.setVisibility(View.GONE);
    }

    protected void onBackClick(View view) {
        mContext.finish();
    }

    protected boolean needFitsSystemWindows() {
        return false;
    }

    protected void setToolbarColor(int color) {
        toolbar.setBackgroundColor(getResources().getColor(color));
    }

    /**
     * 覆盖设置标题方法,应用到自定义标题TextView中
     *
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        tvToolbarTitle.setText(title);
    }


    /**
     * 覆盖设置标题方法,应用到自定义标题TextView中
     *
     * @param titleId
     */
    @Override
    public void setTitle(int titleId) {
        tvToolbarTitle.setText(titleId);
    }

    /**
     * 覆盖设置标题颜色方法,应用到自定义标题TextView中
     *
     * @param textColor
     */
    @Override
    public void setTitleColor(int textColor) {
        tvToolbarTitle.setTextColor(textColor);
    }

    /**
     * 添加导航栏右侧菜单
     *
     * @param menuId
     * @param imgResourceId
     */
    public ImageView addMenuItem(int menuId, int imgResourceId) {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(imgResourceId);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(params);
        imageView.setPadding(SizeUtils.dp2px(10), SizeUtils.dp2px(10), SizeUtils.dp2px(10), SizeUtils.dp2px(10));

        llToolbarMenuView.addView(imageView);

        imageView.setTag(menuId);
        imageView.setOnClickListener(view -> {
            int id = (int) view.getTag();
            menuItemClick(id);
        });

        return imageView;
    }

    public void menuItemClick(int menuId) {

    }

}
