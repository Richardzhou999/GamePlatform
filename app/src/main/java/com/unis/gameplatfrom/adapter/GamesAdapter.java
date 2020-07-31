package com.unis.gameplatfrom.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;


import java.util.List;

import com.squareup.picasso.Picasso;
import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.manager.GlideManager;
import com.unis.gameplatfrom.entity.GamesEntity;


public class GamesAdapter extends BaseEmptyViewAdapter<GamesEntity> {

    private int number = 1;
    private List<GamesEntity> list;

    public GamesAdapter(Context mContext, List<GamesEntity> list) {
        super(mContext,true,R.layout.item_games, list);
        this.list = list;
    }


//    @Override
//    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {
//
//
//        Log.e("onBindViewHolder","下载进度列表"+payloads.size());
//        if(payloads.isEmpty()){
//            super.onBindViewHolder(holder, position, payloads);
//            return;
//        }
//
//        int number = (int) payloads.get(0);
//        Log.e("onBindViewHolder","下载进度："+number);
////
////            if(position == number){
////
////
////
////        }
//
////        GamesEntity gamesEntity = list.get(position);
////
//
//
////        ProgressBar progressBar = holder.itemView.findViewById(R.id.item_progress);
////        progressBar.setProgress(number);
//
//        //TextView
//
//
//
//
//
//
//    }

    @Override
    protected void convert(BaseViewHolder holder, GamesEntity entity) {


        RelativeLayout relativeLayout = holder.getView(R.id.game_item_layout);

        relativeLayout.getBackground().setAlpha(50);

        ImageView imageView = holder.getView(R.id.item_games_icon);
        //GlideManager.loadImg(entity.getIcon(), holder.getView(R.id.item_games_icon));
        Picasso.with(mContext).load(entity.getIcon()).placeholder(R.drawable.logo).into(imageView);


        holder.setText(R.id.item_games_name, "游戏名："+entity.getName());


        //holder.setText(R.id.item_games_version, "版本："+entity.getV());

        TextView down = holder.getView(R.id.item_games_down);
        TextView install = holder.getView(R.id.item_games_install);
        TextView newVersion = holder.getView(R.id.item_games_new_version);


        holder.setText(R.id.game_number,""+String.valueOf(holder.getAdapterPosition()+1));

        if(entity.isNewGame()){
            holder.setText(R.id.item_games_version, "版本："+(entity.getV()-1));
            newVersion.setVisibility(View.VISIBLE);
            holder.setText(R.id.item_games_new_version, "发现新版本："+entity.getV());

        }else {
            holder.setText(R.id.item_games_version, "版本："+entity.getV());

            newVersion.setVisibility(View.INVISIBLE);
        }



        if(entity.isDownGame()){

            holder.setVisible(R.id.lin_progress,true);

        }else {

            holder.setVisible(R.id.lin_progress,false);
        }


        holder.setText(R.id.item_progress_number,String.valueOf(entity.getProgress())+"%");


        //holder.setText(R.id.item_progress_kb,
        // speed <= 0 ? "" : String.format("%s/s", CommonUtil.formatFileSize(speed)))


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
