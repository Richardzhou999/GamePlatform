package com.unis.kotlin.api.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.squareup.picasso.Picasso
import com.unis.kotlin.R
import com.unis.kotlin.entity.GamesEntity

class MainAdapter :BaseEmptyViewAdapter<GamesEntity>{

    private var context : Context? = null

    constructor(context: Context,list: ArrayList<GamesEntity>) :
            super(context, R.layout.item_games,list){
        this.context = context
    }


    override fun convert(holder: BaseViewHolder?, entity: GamesEntity?) {

        var relativeLayout =  holder!!.getView<RelativeLayout>(R.id.game_item_layout)
        relativeLayout.background.alpha = 50

        var imageView = holder.getView<ImageView>(R.id.item_games_icon)

        Picasso.with(mContext).load(entity!!.icon).placeholder(R.drawable.logo).into(imageView)

        holder.setText(R.id.item_games_name, "游戏名：" + entity!!.name)

        val down = holder.getView<TextView>(R.id.item_games_down)
        val install = holder.getView<TextView>(R.id.item_games_install)
        val newVersion = holder.getView<TextView>(R.id.item_games_new_version)


        holder.setText(R.id.game_number, "" + (holder.adapterPosition + 1).toString())

        if (entity.isNewGame && entity.newVersion != 0) {
            holder.setText(R.id.item_games_version, "版本：" + entity.newVersion)
            newVersion.visibility = View.VISIBLE
            holder.setText(R.id.item_games_new_version, "发现新版本：" + entity.v)

        } else {
            holder.setText(R.id.item_games_version, "版本：" + entity.v)

            newVersion.visibility = View.INVISIBLE
        }



        if (entity.isDownGame) {

            holder.setVisible(R.id.lin_progress, true)

        } else {

            holder.setVisible(R.id.lin_progress, false)
        }


        holder.setText(R.id.item_progress_number, entity.process.toString() + "%")


        //holder.setText(R.id.item_progress_kb,
        // speed <= 0 ? "" : String.format("%s/s", CommonUtil.formatFileSize(speed)))


        val progressBar = holder.getView<ProgressBar>(R.id.item_progress)
        progressBar.progress = entity.process


        if (entity.isInstallGame && entity.isLocal) {
            install.visibility = View.VISIBLE
            down.visibility = View.INVISIBLE

        } else if (!entity.isInstallGame && entity.isLocal) {

            down.visibility = View.VISIBLE
            install.visibility = View.INVISIBLE

        } else {
            down.visibility = View.INVISIBLE
            install.visibility = View.INVISIBLE
        }


        holder.addOnClickListener(R.id.item_game_down)
        holder.addOnClickListener(R.id.item_game_cancel)




    }
}