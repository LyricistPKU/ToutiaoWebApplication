package com.lyubinbin.model;

/**
 * Created by Lyu binbin on 2016/6/30.
 */
public class Name {
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    public Name(String userId){
        this.userId = userId;
    }
}
