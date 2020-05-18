package com.unis.gameplatfrom.model;

import org.litepal.crud.LitePalSupport;

public class GamesEntity extends LitePalSupport {

     private int g;
     private int v;
     private String p;
     private String icon;
     private String packname;
     private String name;
     private boolean isGame;//测试游戏是否存在
     private boolean isNewGame;//测试是否有新版本



    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
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


    public boolean isGame() {
        return isGame;
    }

    public void setGame(boolean game) {
        isGame = game;
    }

    public boolean isNewGame() {
        return isNewGame;
    }

    public void setNewGame(boolean newGame) {
        isNewGame = newGame;
    }
}
