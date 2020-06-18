package com.unis.gameplatfrom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import java.util.List;

import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.manager.GlideManager;
import com.unis.gameplatfrom.model.GamesEntity;
import com.unis.gameplatfrom.ui.GamesActivity;


public class GamesAdapter extends BaseEmptyViewAdapter<GamesEntity> {

    private int number = 1;
    private List<GamesEntity> list;

    public GamesAdapter(Context mContext, List<GamesEntity> list) {
        super(mContext,true,R.layout.item_games, list);
        this.list = list;
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {


        Log.e("xxxxx","xxxxxx"+payloads.size());
        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads);
            return;
        }

//        int number = (int) payloads.get(0);
//
//            if(position == number){
//
//
//
//        }

//        GamesEntity gamesEntity = list.get(position);
//
//        ProgressBar progressBar = holder.getView(R.id.item_progress);
//        progressBar.setProgress(gamesEntity.getProgress());

        //TextView






    }

    @Override
    protected void convert(BaseViewHolder holder, GamesEntity entity) {

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


        RelativeLayout relativeLayout = holder.getView(R.id.game_item_layout);

        relativeLayout.getBackground().setAlpha(50);

        GlideManager.loadImg(entity.getIcon(), holder.getView(R.id.item_games_icon));

        holder.setText(R.id.item_games_name, "游戏名："+entity.getName());


        //holder.setText(R.id.item_games_version, "版本："+entity.getV());

        TextView down = holder.getView(R.id.item_games_down);
        TextView install = holder.getView(R.id.item_games_install);

        holder.setText(R.id.game_number,String.valueOf(holder.getAdapterPosition()+1));


        if(entity.isNewGame()){
            holder.setText(R.id.item_games_version, "版本："+(entity.getV()-1));
            holder.setVisible(R.id.item_games_new_version,true);
            holder.setText(R.id.item_games_new_version, "发现新版本："+entity.getV());

        }else {
            holder.setText(R.id.item_games_version, "版本："+entity.getV());

            holder.setVisible(R.id.item_games_new_version,false);
        }



        if(entity.isDownGame()){

            holder.setVisible(R.id.lin_progress,true);

        }else {

            holder.setVisible(R.id.lin_progress,false);
        }


        holder.setText(R.id.item_progress_number,String.valueOf(entity.getProgress())+"%");

        ProgressBar progressBar = holder.getView(R.id.item_progress);
        progressBar.setProgress(entity.getProgress());


        if(entity.isInstallGame() && entity.isLocal()){
            install.setVisibility(View.VISIBLE);
            down.setVisibility(View.INVISIBLE);

        }else if(!entity.isInstallGame() && entity.isLocal()){
            down.setVisibility(View.VISIBLE);
            install.setVisibility(View.INVISIBLE);
        }else {
            down.setVisibility(View.INVISIBLE);
            install.setVisibility(View.INVISIBLE);
        }


        holder.addOnClickListener(R.id.item_game_down);
        holder.addOnClickListener(R.id.item_game_cancel);




    }




}
