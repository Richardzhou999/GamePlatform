package com.unis.gameplatfrom.ui;

import android.animation.Animator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.adapter.MainAdapter;
import com.unis.gameplatfrom.api.HUDLoadDataSubscriber;
import com.unis.gameplatfrom.api.PublicApiInterface;
import com.unis.gameplatfrom.api.RetrofitWrapper;
import com.unis.gameplatfrom.base.BaseActivity;
import com.unis.gameplatfrom.cache.UserCenter;
import com.unis.gameplatfrom.model.GamesEntity;
import com.unis.gameplatfrom.model.LoginResult;
import com.unis.gameplatfrom.ui.widget.MetroViewBorderHandler;
import com.unis.gameplatfrom.ui.widget.MetroViewBorderImpl;
import com.unis.gameplatfrom.utils.udateapk.LinPermission;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {


    @BindView(R.id.user_account)
    TextView userAccount;

    @BindView(R.id.draw_order_layout)
    ViewGroup drawLayout;

    private MainAdapter mainAdapter;

    private List<GamesEntity> list  = new ArrayList<>();

    private MetroViewBorderImpl metroViewBorderImpl;


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {



//        RetrofitWrapper.getInstance().create(PublicApiInterface.class)
//                .passwordLogin(account,password,EncryptUtils.encryptMD5ToString(account+password+"UNIS").toLowerCase())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribe(new HUDLoadDataSubscriber<LoginResult<GamesEntity>>(mContext) {
//                    @Override
//                    public void onNext(LoginResult<GamesEntity> result) {
//
//                        if (LinPermission.checkPermission(MainActivity.this, new int[]{7, 8})) {
//
//                            mainAdapter.setNewData(result.getGame());
//
//                        }
//                    }
//                });




    }

    @Override
    protected void initView(Bundle savedInstanceState) {



        userAccount.setText(UserCenter.getInstance().getToken());

        // finish();

        if(LinPermission.checkPermission(MainActivity.this, new int[]{7,8})){

            Intent intent = new Intent(MainActivity.this,GamesActivity.class);

            startActivity(intent);
            finish();

        }else {

            //首先申请权限
            LinPermission.requestMultiplePermission(MainActivity.this,new int[]{7,8});
        }







//        FrameLayout roundedFrameLayout = new FrameLayout(this);
//        roundedFrameLayout.setClipChildren(false);
//
//        metroViewBorderImpl = new MetroViewBorderImpl(roundedFrameLayout);
//        metroViewBorderImpl.setBackgroundResource(R.drawable.border_color);
//
//
//        metroViewBorderImpl.attachTo(drawLayout);
//
//        metroViewBorderImpl.getViewBorder().addOnFocusChanged(new MetroViewBorderHandler.FocusListener() {
//            @Override
//            public void onFocusChanged(View oldFocus, final View newFocus) {
//                metroViewBorderImpl.getView().setTag(newFocus);
//
//            }
//        });
//        metroViewBorderImpl.getViewBorder().addAnimatorListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                View t = metroViewBorderImpl.getView().findViewWithTag("top");
//                if (t != null) {
//                    ((ViewGroup) t.getParent()).removeView(t);
//                    View of = (View) metroViewBorderImpl.getView().getTag(metroViewBorderImpl.getView().getId());
//                    ((ViewGroup) of).addView(t);
//                }
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                View nf = (View) metroViewBorderImpl.getView().getTag();
//                if (nf != null) {
//                    View top = nf.findViewWithTag("top");
//                    if (top != null) {
//                        ((ViewGroup) top.getParent()).removeView(top);
//                        ((ViewGroup) metroViewBorderImpl.getView()).addView(top);
//                        metroViewBorderImpl.getView().setTag(metroViewBorderImpl.getView().getId(), nf);
//
//                    }
//                }
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });


    }


    @OnClick({R.id.game_view})
    public void OnClick(View view){

        switch (view.getId()){

            case R.id.game_view:

                if (LinPermission.checkPermission(MainActivity.this, new int[]{7,8})) {


                    Intent intent = new Intent(MainActivity.this,GamesActivity.class);

                    startActivity(intent);
                    finish();
                }

                break;

            default:
                break;





        }





    }







    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LinPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, new LinPermission.PermissionsResultListener() {
            @Override
            public void onRequestPermissionSuccess(int pos, String permission) {


                Intent intent = new Intent(MainActivity.this,GamesActivity.class);



                startActivity(intent);
                finish();


            }

            @Override
            public void onRequestPermissionFailure(int pos, String permission) {
                finish();
            }
        });
    }
}
