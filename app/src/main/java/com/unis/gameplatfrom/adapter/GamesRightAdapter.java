package com.unis.gameplatfrom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.manager.GlideManager;
import com.unis.gameplatfrom.model.GamesEntity;

import java.util.List;


public class GamesRightAdapter extends BaseEmptyViewAdapter<GamesEntity> {



    public GamesRightAdapter(Context mContext, List<GamesEntity> list) {
        super(mContext,true,R.layout.item_games_right, list);
    }

    @Override
    protected void convert(BaseViewHolder holder, GamesEntity entity) {

        GlideManager.loadImg(entity.getIcon(), holder.getView(R.id.item_games_icon));

        holder.setText(R.id.item_games_name, "游戏名："+entity.getName());


        //holder.setText(R.id.item_games_version, "版本："+entity.getV());

        TextView down = holder.getView(R.id.item_games_down);


        if(entity.isNewGame()){
            holder.setText(R.id.item_games_version, "版本："+(entity.getV()-1));
            holder.setVisible(R.id.item_games_new_version,true);
            holder.setText(R.id.item_games_new_version, "发现新版本："+entity.getV());

        }else {
            holder.setText(R.id.item_games_version, "版本："+entity.getV());
            holder.setVisible(R.id.item_games_new_version,false);
        }


        if(entity.isGame()){
            down.setVisibility(View.VISIBLE);
        }else {
            down.setVisibility(View.INVISIBLE);
        }


//        GlideManager.loadImg(entity.getIcon(), holder.getView(R.id.item_games_icon));
//        StringBuilder builder = new StringBuilder();
//        builder.append(entity.getName());
//        holder.setText(R.id.item_games_name, builder.reverse().toString()+"：名戏游");
//
//
//        //holder.setText(R.id.item_games_version, "版本："+entity.getV());
//
//
//
//        if(entity.isNewGame()){
//            holder.setText(R.id.item_games_version, (entity.getV()-1)+":本版");
//            holder.setVisible(R.id.item_games_new_version,true);
//            holder.setText(R.id.item_games_new_version, entity.getV()+":本版新现发");
//
//        }else {
//            holder.setText(R.id.item_games_version, entity.getV()+":本版");
//            holder.setVisible(R.id.item_games_new_version,false);
//        }
//
//
//        if(entity.isGame()){
//            holder.setVisible(R.id.item_games_down,true);
//        }else {
//            holder.setVisible(R.id.item_games_down,false);
//        }



    }
}
