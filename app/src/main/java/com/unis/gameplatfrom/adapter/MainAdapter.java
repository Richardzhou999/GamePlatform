package com.unis.gameplatfrom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.manager.GlideManager;
import com.unis.gameplatfrom.model.GamesEntity;

import java.util.List;

public class MainAdapter extends BaseEmptyViewAdapter<GamesEntity> implements RecyclerView.ChildDrawingOrderCallback {


    public MainAdapter(Context ctx, List<GamesEntity> list) {
        super(ctx, R.layout.item_main, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, GamesEntity item) {

        ImageView imageView = helper.getView(R.id.item_main_image);

        GlideManager.loadImg(item.getIcon(), imageView);





    }

    @Override
    public int onGetChildDrawingOrder(int childCount, int i) {
        return 0;
    }
}
