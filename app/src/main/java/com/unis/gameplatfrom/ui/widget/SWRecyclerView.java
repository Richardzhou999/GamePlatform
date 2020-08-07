package com.unis.gameplatfrom.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jetbrains.annotations.Nullable;

public class SWRecyclerView extends RecyclerView {

    private static final String TAG = SWRecyclerView.class.getSimpleName();
    private boolean mSelectedItemCentered = true;
    private int mSelectedItemOffsetStart;
    private int mSelectedItemOffsetEnd;
    private int offset = -1;
    private int mDuration = 0;

    public SWRecyclerView(Context context) {
        super(context);
    }

    public SWRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SWRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        // 获取焦点框居中的位置
        if (null != child) {
            if (mSelectedItemCentered) {
                mSelectedItemOffsetStart = !isVertical() ? (getFreeWidth() - child.getWidth()) : (getFreeHeight() - child.getHeight());
                mSelectedItemOffsetStart /= 2;
                mSelectedItemOffsetEnd = mSelectedItemOffsetStart;
            }
        }
        super.requestChildFocus(child, focused);
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        final int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();
        final int parentRight = getWidth() - getPaddingRight();
        final int parentBottom = getHeight() - getPaddingBottom();

        final int childLeft = child.getLeft() + rect.left;
        final int childTop = child.getTop() + rect.top;
        final int childRight = childLeft + rect.width();
        final int childBottom = childTop + rect.height();

        final int offScreenLeft = Math.min(0, childLeft - parentLeft - mSelectedItemOffsetStart);
        final int offScreenTop = Math.min(0, childTop - parentTop - mSelectedItemOffsetStart);
        final int offScreenRight = Math.max(0, childRight - parentRight + mSelectedItemOffsetEnd);
        final int offScreenBottom = Math.max(0, childBottom - parentBottom + mSelectedItemOffsetEnd);

        final boolean canScrollHorizontal = getLayoutManager().canScrollHorizontally();
        final boolean canScrollVertical = getLayoutManager().canScrollVertically();

        // Favor the "start" layout direction over the end when bringing one side or the other
        // of a large rect into view. If we decide to bring in end because start is already
        // visible, limit the scroll such that start won't go out of bounds.
        final int dx;
        if (canScrollHorizontal) {
            if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                dx = offScreenRight != 0 ? offScreenRight
                        : Math.max(offScreenLeft, childRight - parentRight);
            } else {
                dx = offScreenLeft != 0 ? offScreenLeft
                        : Math.min(childLeft - parentLeft, offScreenRight);
            }
        } else {
            dx = 0;
        }

        // Favor bringing the top into view over the bottom. If top is already visible and
        // we should scroll to make bottom visible, make sure top does not go out of bounds.
        final int dy;
        if (canScrollVertical) {
            dy = offScreenTop != 0 ? offScreenTop : Math.min(childTop - parentTop, offScreenBottom);
        } else {
            dy = 0;
        }

        offset = isVertical() ? dy : dx;

        if (dx != 0 || dy != 0) {
            if (mDuration == 0)
                smoothScrollBy(dx, dy);
            else
                smoothScrollBy(dx, dy, mDuration);
            Log.i(TAG, "requestChildRectangleOnScreen: immediate--->"+immediate);
            return true;
        }

        // 重绘是为了选中item置顶，具体请参考getChildDrawingOrder方法
        postInvalidate();

        return false;
    }

    private int getFreeWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getFreeHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    /**
     * 设置默认选中.
     */
    public void setDefaultSelect(int pos) {
        ViewHolder vh = (ViewHolder) findViewHolderForAdapterPosition(pos);
        //requestFocusFromTouch();
        if (vh != null) {
            vh.itemView.requestFocus();
        }
    }

    /**
     * 设置SWRecyclerView的滑动速度
     * @param duration
     */
    public void setDuration(int duration){
        mDuration = duration;
    }

    /**
     * 设置选中的Item居中；
     * @param isCentered
     */
    public void setSelectedItemAtCentered(boolean isCentered) {
        this.mSelectedItemCentered = isCentered;
    }

    /**
     * 判断是垂直，还是横向.
     */
    private boolean isVertical() {
        LinearLayoutManager layout = (LinearLayoutManager) getLayoutManager();
        return layout.getOrientation() == LinearLayoutManager.VERTICAL;
    }

    /**
     * 利用反射拿到RecyclerView中的mViewFlinger属性，
     * 再调用其smoothScrollBy(int dx, int dy, int duration) 方法实现RecyclerViewTV速度的控制
     * @param dx
     * @param dy
     * @param duration
     */
    public void smoothScrollBy(int dx, int dy, int duration) {
        try {
            Class<?> c = null;
            try {
                c = Class.forName("android.support.v7.widget.RecyclerView");//获得Class对象
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return;
            }
            Field mLayoutField = c.getDeclaredField("mLayout");     //根据属性名称，获得类的属性成员Field
            mLayoutField.setAccessible(true);                       //设置为可访问状态
            LayoutManager mLayout = null;
            try {
                mLayout = (LayoutManager) mLayoutField.get(this);   //获得该属性对应的对象
                if(mLayout == null){
                    return;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
            Field mLayoutFrozen = c.getDeclaredField("mLayoutFrozen");
            mLayoutFrozen.setAccessible(true);
            try {
                if((Boolean)mLayoutFrozen.get(this)){
                    return;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
            if (!mLayout.canScrollHorizontally()) {
                dx = 0;
            }
            if (!mLayout.canScrollVertically()) {
                dy = 0;
            }
            Field mViewFlingerField = c.getDeclaredField("mViewFlinger");
            mViewFlingerField.setAccessible(true);
            try {
                Class<?> ViewFlingerClass = null;
                try {
                    //由于内部类是私有的，所以不能直接得到内部类名，
                    //通过mViewFlingerField.getType().getName()
                    //可以得到私有内部类的完整类名
                    ViewFlingerClass = Class.forName(mViewFlingerField.getType().getName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }

                //根据方法名，获得我们的目标方法对象。第一个参数是方法名，后面的是该方法的入参类型。
                // 注意Integer.class与int.class的不同。
                Method smoothScrollBy = ViewFlingerClass.getDeclaredMethod("smoothScrollBy",
                        int.class, int.class, int.class);
                smoothScrollBy.setAccessible(true);//设置为可操作状态
                if (dx != 0 || dy != 0) {
                    Log.d("MySmoothScrollBy", "dx="+dx + " dy="+dy);
                    try {
                        //唤醒（调用）方法，
                        // mViewFlingerField.get(this)指明是哪个对象调用smoothScrollBy。
                        // dx, dy, duration 是smoothScrollBy所需参数
                        smoothScrollBy.invoke(mViewFlingerField.get(this), dx, dy, duration);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return;
            }
        }catch (NoSuchFieldException e){
            return;
        }
    }



}
