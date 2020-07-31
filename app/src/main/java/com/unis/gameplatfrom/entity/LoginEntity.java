package com.unis.gameplatfrom.entity;

import com.unis.gameplatfrom.api.result.BaseResult;

public class LoginEntity extends BaseResult {


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
