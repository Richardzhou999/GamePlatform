package com.unis.gameplatfrom.api.result;

import java.util.List;

/**
 * Created by wulei on 2016-10-22.
 */

@SuppressWarnings("DefaultFileTemplate")
public class BaseListResult<T> extends BaseResult {

    private int page;
    private int size;
    private int totalPage;
    private int totalSize;
    private List<T> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
