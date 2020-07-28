package com.unis.gameplatfrom.model;

import com.unis.gameplatfrom.api.result.BaseResult;

import java.util.List;

public class LoginResult extends BaseResult{


    private String uuid;
    private String name;
    private String head;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }
}
