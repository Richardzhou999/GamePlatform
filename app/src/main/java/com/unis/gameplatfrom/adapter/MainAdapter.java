package com.unis.gameplatfrom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.manager.GlideManager;
import com.unis.gameplatfrom.entity.GamesEntity;

import java.util.List;

public class MainAdapter extends BaseEmptyViewAdapter<GamesEntity>  {


    private int number = 1;
    private List<GamesEntity> list;
    private Context mContext;

    public MainAdapter(Context mContext, List<GamesEntity> list) {
        super(mContext,true,R.layout.item_main, list);
        this.mContext = mContext;
        this.list = list;
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {


        Log.e("xxxxx","xxxxxx"+payloads.size());
        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position);

        }else {



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

        ImageView imageView = holder.getView(R.id.item_games_icon);

       // GlideManager.loadImg(entity.getIcon(), holder.getView(R.id.item_games_icon));
        Picasso.with(mContext)
                .load(entity.getIcon())
                .placeholder(R.drawable.logo)
                .into(imageView);

        holder.setText(R.id.item_games_name, "游戏名："+entity.getName());


        //holder.setText(R.id.item_games_version, "版本："+entity.getV());

        TextView down = holder.getView(R.id.item_games_down);
        TextView install = holder.getView(R.id.item_games_install);
        TextView newVersion = holder.getView(R.id.item_games_new_version);
        holder.setText(R.id.game_number,String.valueOf(holder.getAdapterPosition()+1));


        if(entity.isNewGame() && entity.getNewVersion() != 0 ){
            holder.setText(R.id.item_games_version, "版本："+(entity.getNewVersion()));
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
    }



}
