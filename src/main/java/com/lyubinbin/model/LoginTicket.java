package com.lyubinbin.model;

import java.util.Date;

/**
 * user ticket(cookie) model
 * Created by nowcoder on 2016/7/3.
 */

public class LoginTicket {
    private int id;     // ticket id
    private int userId;     // user id
    private Date expired;   // expired date
    private int status;     // 0: valid, 1: invalid
    private String ticket;      // ticket string

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
