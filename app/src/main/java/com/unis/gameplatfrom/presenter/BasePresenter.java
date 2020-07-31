package com.unis.gameplatfrom.presenter;

import android.content.Context;

import com.unis.gameplatfrom.model.BaseModel;
import com.unis.gameplatfrom.ui.view.BaseView;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<M extends BaseModel,V extends BaseView> {

    private WeakReference<V> mvpView;
    private M mvpModel;


    /**
     * 绑定view，一般在初始化中调用该方法
     *
     * @param view view
     */
    @SuppressWarnings("unchecked")
    public void attachView(V view) {
        this.mvpView = new WeakReference<V>(view);
        if(mvpModel == null){
            mvpModel = createModule();
        }
    }

    /**
     * 解除绑定view，一般在onDestroy中调用
     */

    public void detachView() {
        if (null != mvpView) {
            mvpView.clear();
            mvpView = null;
        }
        this.mvpModel = null;
    }

    /**
     * View是否绑定
     *
     * @return
     */
    public boolean isViewAttached() {
        return null != mvpView && null != mvpView.get();
    }

    protected M getModule() {
        return mvpModel;
    }

    protected V getView() {
        return isViewAttached() ? mvpView.get() : null;
    }


    /**
     * 通过该方法创建Module
     */
    protected abstract M createModule();

    /**
     * 初始化方法
     */
    public abstract void start();



}
