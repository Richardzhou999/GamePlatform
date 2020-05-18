package com.unis.gameplatfrom.api.result;

import java.util.List;

/**
 * Created by wulei on 2016-10-22.
 */

@SuppressWarnings("DefaultFileTemplate")
public class BaseCustomListResult<T> extends BaseResult {

    private int err;
    private List<T> game;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public List<T> getData() {
        return game;
    }

    public void setData(List<T> data) {
        this.game = game;
    }
}
