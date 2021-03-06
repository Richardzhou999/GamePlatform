package com.unis.gameplatfrom.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.thejoyrun.router.Router;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.unis.gameplatfrom.presenter.BasePresenter;
import com.unis.gameplatfrom.ui.view.BaseView;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by wulei on 2017/3/27.
 */

@SuppressWarnings("DefaultFileTemplate")
public abstract class BaseFragment<P extends BasePresenter> extends RxFragment implements BaseView{
    protected Activity mContext;
    private Unbinder mUnbinder;

    protected P mPresenter;

    protected abstract int getLayout();

    protected abstract void initView(View var1, Bundle var2);

    protected abstract void initData();

    /**
     * 初始化mPresenter
     */
    protected abstract void initPresenter();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(this.getLayout(), container, false);
        mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.mUnbinder = ButterKnife.bind(this, view);
        //初始化mPresenter
        initPresenter();
        //绑定view
        if(mPresenter != null){
            mPresenter.attachView(this);
        }


        Router.inject(mContext);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
        initData();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    protected void showMessageDialog(String msg) {
        Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if(mPresenter != null){
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * 当Fragment可见时才加载数据
     */
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        initData();
//    }
}
