package com.unis.gameplatfrom.api.result;

import java.util.List;

/**
 * Created by wulei on 2016-10-22.
 */

@SuppressWarnings("DefaultFileTemplate")
public class BaseCustomListResult<T> extends BaseResult {


    private List<T> glist ;

    public List<T> getData() {
        return glist ;
    }

    public void setData(List<T> data) {
        this.glist  = glist ;
    }
}
