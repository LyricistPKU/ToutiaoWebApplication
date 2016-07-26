package com.lyubinbin.model;

import org.springframework.stereotype.Component;

/**
 * current user, user @component to initialize at start
 * Created by Lyu binbin on 2016/7/25.
 */
@Component
public class Hostholder {
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
