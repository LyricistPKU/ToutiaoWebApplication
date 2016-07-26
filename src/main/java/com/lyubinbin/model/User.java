package com.lyubinbin.model;

/**
 * user model
 * Created by Lyu binbin on 2016/7/3.
 */
public class User {
    private int id;     // user id
    private String name;    // user name
    private String password;    // user password
    private String salt;    // random generated salt
    private String headUrl;     // head pic url, stored in qiniu cloud server

    public User() {}

    public User(String name) {
        this.name = name;
        this.password = "";
        this.salt = "";
        this.headUrl = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
