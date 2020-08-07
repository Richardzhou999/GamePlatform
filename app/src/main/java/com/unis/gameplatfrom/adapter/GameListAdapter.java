package com.unis.gameplatfrom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.AbsEntity;
import com.arialyy.aria.core.download.DownloadGroupEntity;
import com.arialyy.aria.core.inf.IEntity;
import com.unis.gameplatfrom.R;
import com.unis.gameplatfrom.base.AbsHolder;
import com.unis.gameplatfrom.base.AbsRVAdapter;
import com.unis.gameplatfrom.entity.GamesEntity;
import com.unis.gameplatfrom.entity.GamesListEntity;
import com.unis.gameplatfrom.manager.GlideManager;
import com.unis.gameplatfrom.ui.GamesListActivity;
import com.unis.gameplatfrom.utils.PackageUtil;
import com.unis.gameplatfrom.utils.udateapk.DownloadAPk;
import com.unis.gameplatfrom.utils.udateapk.LinPermission;

import org.litepal.LitePal;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GameListAdapter extends AbsRVAdapter<GamesListEntity, GameListAdapter.SimpleHolder> {


    private List<GamesListEntity> data;
    private Context mContext;

    private Map<String, Integer> mPositions = new ConcurrentHashMap<>();


    public GameListAdapter(Context context,List<GamesListEntity> data) {
        super(context, data);
        this.data = data;
        this.mContext = context;

        int i = 0;
        for (GamesListEntity entity : data) {
            mPositions.put(entity.getP(), i);
            i++;
        }
    }



    @Override
    protected SimpleHolder getViewHolder(View convertView, int viewType) {
        return new SimpleHolder(convertView);
    }

    @Override
    protected int setLayoutId(int type) {
        return R.layout.activity_games_3d;
    }



    private String getKey(AbsEntity entity) {
        if (entity instanceof GamesListEntity) {
            return ((GamesListEntity) entity).getP();
        } else if (entity instanceof DownloadGroupEntity) {
            return ((DownloadGroupEntity) entity).getGroupHash();
        }
        return "";
    }


    @Override
    protected void bindData(SimpleHolder holder, int position, GamesListEntity entity) {

        RelativeLayout relativeLayout = holder.gameLayout;

        relativeLayout.getBackground().setAlpha(50);

        GlideManager.loadImg(entity.getIcon(), holder.gameIcon);

        holder.gameName.setText("游戏名："+entity.getName());


        //holder.setText(R.id.item_games_version, "版本："+entity.getV());

        holder.gameNumber.setText(String.valueOf(holder.getAdapterPosition()+1));






//        if(entity.isDownGame()){
//
//            holder.progressLayout.setVisibility(View.VISIBLE);
//
//        }else {
//
//            holder.progressLayout.setVisibility(View.GONE);
//        }


        holder.progressNumber.setText(String.valueOf(entity.getProgress())+"%");


        holder.progress.setProgress(entity.getProgress());



        //点击下载
        holder.gameDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                GamesListEntity entity = (GamesListEntity) data.get(position);

                if (entity.getV() != 0) {

                        /**
                         * 情况1：记录不在，游戏在
                         * 情况2：记录不在，游戏不在
                         * 情况3：两者都在
                         */
                        GamesEntity entity1 = LitePal.where("id="+entity.getId()).findFirst(GamesEntity.class);
                        if(entity1 != null){

                            //若游戏被删除，需清除游戏记录防止数据出错
                            if (PackageUtil.isAppByPackageID(mContext,entity.getPackname())) {


                                System.out.print(entity.getV()+"");
                                int number = entity.getV();


                                if(number > entity1.getV()){

                                    entity1.setV(entity.getV());

                                    entity1.save();
                                    downApk(entity.getName(),entity.getP(),entity.getIcon(),position);


                                }else {

                                    entity.setDownGame(false);
//                                    PackageUtil.startAppByPackageID(mContext,entity.getPackname(),
//                                            entity.getId(),);

                                }



                            }else {

                                entity1.setV(entity.getV());
                                entity1.setName(entity.getName());
                                entity1.setP(entity.getP());
                                entity1.setPackname(entity.getPackname());
                                entity1.setIcon(entity.getIcon());
                                entity1.save();
                                entity.setDownGame(true);
                                downApk(entity.getName(), entity.getP(), entity.getIcon(),position);

                            }


                        }else {


                            if(PackageUtil.isAppByPackageID(mContext,entity.getPackname())){

                                //entity.setAccount(game_account);
                                entity.save();
                                entity.setDownGame(false);
                               // PackageUtil.startAppByPackageID(mContext,entity.getPackname());

                            }else {

                                //第一次下载

                                //entity.setAccount(game_account);
                                //entity.save();
                                entity.setDownGame(true);
                                downApk(entity.getName(),entity.getP(),entity.getIcon(),position);

                            }

                        }

                }
            }
        });

        holder.gameCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Aria.download(mContext)
                        .load(entity.getId())
                        .cancel(true);
            }
        });

        holder.gameStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = entity.getId();
                Log.e("暂停xxx",""+id);
                Aria.download(mContext)
                        .load(entity.getId())
                        .stop();
            }
        });

        holder.gameRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Aria.download(mContext)
                        .load(entity.getId())
                        .resume();
            }
        });

    }

    @Override
    protected void bindData(SimpleHolder holder, int position, GamesListEntity item,
                                      List<Object> payloads) {
        AbsEntity entity = (AbsEntity) payloads.get(0);
        updateSpeed(holder, entity);
    }


    /**
     * 只更新速度
     */
    private void updateSpeed(SimpleHolder holder, final AbsEntity entity) {


        long size = entity.getFileSize();
        long progress = entity.getCurrentProgress();
        int current = size == 0 ? 0 : (int) (progress * 100 / size);
        holder.progress.setProgress(current);

        //if (holder instanceof GroupHolder){
        //  handleSubChild((GroupHolder) holder, entity);
        //}
    }

    public synchronized void updateState(GamesListEntity entity) {

        if (entity.getState() == IEntity.STATE_CANCEL) {
            mData.remove(entity);
            mPositions.clear();
            int i = 0;
            for (GamesListEntity entity_1 : mData) {
                mPositions.put(getKey(entity), i);
                i++;
            }
            notifyDataSetChanged();
        } else {
            int position = indexItem(getKey(entity));
            if (position == -1 || position >= mData.size()) {
                return;
            }

            mData.set(position,  entity);
            notifyItemChanged(position);
        }
    }

    /**
     * 更新进度
     */
    public synchronized void setProgress(GamesListEntity entity) {
        String url = entity.getKey();
        int position = indexItem(url);
        if (position == -1 || position >= mData.size()) {
            return;
        }
        mData.set(position,  entity);
        notifyItemChanged(position, entity);
    }

    private synchronized int indexItem(String url) {
        Set<String> keys = mPositions.keySet();
        for (String key : keys) {
            if (key.equals(url)) {
                return mPositions.get(key);
            }
        }
        return -1;
    }


    private void downApk(String name,String filepath,String iconUrl,int positoin){


        if(filepath.contains("apk")){

            String[] apk_path = filepath.split("/");


            String path = Environment.getExternalStorageDirectory()
                    + "/DownLoad/"+apk_path[apk_path.length-1];



                if (PackageUtil.isAppByLocal(filepath)) {


                    Intent installAppIntent = DownloadAPk.getInstallAppIntent(mContext, path);
                    mContext.startActivity(installAppIntent);


                } else {

                    //Aria.get(mContext).
                    long id = Aria.download(mContext)
                            .load(filepath)
                            .setFilePath(path)
                            .create();
                    Log.e("xxxxx",""+id);
                }


        }else {

            Toast.makeText(mContext,"游戏未上传,请跟客服人员联系",Toast.LENGTH_SHORT).show();

        }



    }




    class SimpleHolder extends AbsHolder {

        RelativeLayout gameLayout;
        LinearLayout progressLayout;
        ProgressBar progress;
        TextView gameNumber;
        ImageView gameIcon;
        TextView gameName;
        TextView gameVersion;
        TextView gameDown;
        TextView gameStop;
        TextView gameRestart;
        TextView gameCancel;


        TextView progressNumber;

        SimpleHolder(View itemView) {
            super(itemView);

            gameLayout = itemView.findViewById(R.id.game_item_layout);
            progressLayout = itemView.findViewById(R.id.lin_progress);
            gameNumber = itemView.findViewById(R.id.game_number);
            progress = itemView.findViewById(R.id.item_progress);
            gameIcon = itemView.findViewById(R.id.item_games_icon);
            gameName = itemView.findViewById(R.id.item_games_name);
            gameVersion = itemView.findViewById(R.id.item_games_version);
            gameDown = itemView.findViewById(R.id.item_game_down);
            gameStop = itemView.findViewById(R.id.item_game_stop);
            gameRestart = itemView.findViewById(R.id.item_game_restart);
            gameCancel = itemView.findViewById(R.id.item_game_cancel);


            progressNumber = itemView.findViewById(R.id.item_progress_number);

        }
    }

    class GroupHolder extends SimpleHolder {

        GroupHolder(View itemView) {
            super(itemView);
        }
    }



}
