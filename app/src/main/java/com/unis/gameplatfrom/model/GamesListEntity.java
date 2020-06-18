package com.unis.gameplatfrom.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;




import org.litepal.crud.LitePalSupport;


public class GamesListEntity extends LitePalSupport{

     private int id;
     private int v;
     private String p;
     private String icon;
     private String packname;
     private String name;
     private String account;//判断用户的游戏
     private boolean isInstallGame;//测试游戏是否已安装
     private boolean isDownGame;//是否开始下载
     private boolean isLocal;//判断游戏是否存在本地
     private boolean isNewGame;//测试是否有新版本

     private int progress;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public boolean isInstallGame() {
        return isInstallGame;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setInstallGame(boolean installGame) {
        isInstallGame = installGame;
    }

    public boolean isDownGame() {
        return isDownGame;
    }

    public void setDownGame(boolean downGame) {
        isDownGame = downGame;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public boolean isNewGame() {
        return isNewGame;
    }

    public void setNewGame(boolean newGame) {
        isNewGame = newGame;
    }
}
