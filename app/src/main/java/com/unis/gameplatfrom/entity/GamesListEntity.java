package com.unis.gameplatfrom.entity;


import android.os.Parcel;
import android.os.Parcelable;

import com.arialyy.aria.core.common.AbsNormalEntity;
import com.arialyy.aria.core.download.DownloadEntity;

import org.litepal.crud.LitePalSupport;


public class GamesListEntity extends DownloadEntity implements Parcelable {

     private int id;
     private int v;
     private String p;
    private int gameId;
     private String icon;
     private String packname;
     private String name;
     private String account;//判断用户的游戏
     private boolean isInstallGame;//测试游戏是否已安装
     private boolean isDownGame;//是否开始下载
     private boolean isLocal;//判断游戏是否存在本地
     private boolean isNewGame;//测试是否有新版本

     private int progress;






    public int getGameId() {
        return id;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public int getTaskType() {
        return 0;
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

    @Override
    public String getFilePath() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected GamesListEntity(Parcel in) {
        super(in);
        id = in.readInt();
        v = in.readInt();
        p = in.readString();
        gameId = in.readInt();
        icon = in.readString();
        packname = in.readString();
        name = in.readString();
        account = in.readString();
        isInstallGame = in.readByte() != 0;
        isDownGame = in.readByte() != 0;
        isLocal = in.readByte() != 0;
        isNewGame = in.readByte() != 0;
        progress = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(id);
        dest.writeInt(v);
        dest.writeString(p);
        dest.writeInt(gameId);
        dest.writeString(icon);
        dest.writeString(packname);
        dest.writeString(name);
        dest.writeString(account);
        dest.writeByte((byte) (isInstallGame ? 1 : 0));
        dest.writeByte((byte) (isDownGame ? 1 : 0));
        dest.writeByte((byte) (isLocal ? 1 : 0));
        dest.writeByte((byte) (isNewGame ? 1 : 0));
        dest.writeInt(progress);
    }

    public static final Creator<GamesListEntity> CREATOR = new Creator<GamesListEntity>() {
        @Override
        public GamesListEntity createFromParcel(Parcel in) {
            return new GamesListEntity(in);
        }

        @Override
        public GamesListEntity[] newArray(int size) {
            return new GamesListEntity[size];
        }
    };

}
