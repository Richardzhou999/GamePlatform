package com.unis.gameplatfrom.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.thejoyrun.router.Router;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.unis.gameplatfrom.AppManager;
import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.utils.StatusBarUtil;


/**
 * Created by wulei on 2017/3/27.
 */

@SuppressWarnings("DefaultFileTemplate")
public abstract class BaseActivity extends RxAppCompatActivity {
    protected Activity mContext;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //4.4+透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (hasTransparencyStatusBar()) {
                StatusBarUtil.transparencyBar(this);
            } else {
                StatusBarUtil.setStatusBarColor(this, getStatusBarColor());
            }
            StatusBarUtil.StatusBarLightMode(this);
        }

        mContext = this;
        AppManager.getAppManager().addActivity(this);

        registEventBus();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        if (getLayout() > 0) {
//            setContentView(getLayout());
//        }
        this.mUnbinder = ButterKnife.bind(this);
        Router.inject(this);
        initView(savedInstanceState);
        initData();
    }


    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
//        友盟
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    @CallSuper
    protected void onPause() {
        super.onPause();
        //        友盟
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

    @Override
    @CallSuper
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unRegistEventBus();
        mUnbinder.unbind();
        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }

    @LayoutRes
    protected abstract int getLayout();

    protected abstract void initData();

    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 状态栏颜色
     *
     * @return
     */
    protected int getStatusBarColor() {
        return R.color.white;
    }

    /**
     * 是否透明状态栏
     *
     * @return
     */
    protected boolean hasTransparencyStatusBar() {
        return false;
    }

    protected void registEventBus() {
        //子类如果需要注册eventbus，则重写此方法
        //EventBus.getDefault().register(this);
    }

    protected void unRegistEventBus() {
        //子类如果需要注销eventbus，则重写此方法
        //EventBus.getDefault().unregister(this);
    }

    /**
     * 点击EditTextw外隐藏软键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void showMessageDialog(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void delayFinish(long millisTime) {
        new Handler().postDelayed(() -> {
            if (mContext != null) {
                mContext.finish();
            }
        }, millisTime);
    }
}
