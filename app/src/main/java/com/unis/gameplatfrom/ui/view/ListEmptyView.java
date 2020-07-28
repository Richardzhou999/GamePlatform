package com.unis.gameplatfrom.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unis.gameplatfrom.R;


/**
 * Created by wulei on 2016-08-16.
 */

@SuppressWarnings("DefaultFileTemplate")
public class ListEmptyView extends RelativeLayout {
    private ImageView ivEmpeyt;
    private TextView tvMsg;

    public ListEmptyView(Context context) {
        super(context);
        initWidthContext(context);
    }

    public ListEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWidthContext(context);
    }

    public ListEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWidthContext(context);
    }

    private void initWidthContext(Context ctx) {
        LayoutInflater.from(ctx).inflate(R.layout.list_empty_view, this, true);

        ivEmpeyt = (ImageView) findViewById(R.id.iv_empty_image);
        tvMsg = (TextView) findViewById(R.id.tv_enpty_view_msg);
    }

    public void setMessage(String msg) {
        tvMsg.setText(msg);
    }

    public void setImageResource(int resource) {
        ivEmpeyt.setImageResource(resource);
    }
}
