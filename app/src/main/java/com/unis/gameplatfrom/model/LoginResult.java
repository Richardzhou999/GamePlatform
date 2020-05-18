package com.unis.gameplatfrom.model;

import com.unis.gameplatfrom.api.result.BaseResult;

import java.util.List;

public class LoginResult<T>{

    private int err;
    private String msg;
    private String uuid;
    private List<T> game ;


    public int getErr() {
        return err;
    }


    public void setErr(int err) {
        this.err = err;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<T> getGame() {
        return game;
    }

    public void setGame(List<T> game) {
        this.game = game;
    }

}
