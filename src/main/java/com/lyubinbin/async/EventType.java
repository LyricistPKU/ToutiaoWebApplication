package com.lyubinbin.async;

/**
 * Created by Lyu binbin on 2016/7/20.
 */
public enum  EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private  int value;
    EventType(int value){this.value = value;}

    public int getValue(){
        return value;
    }
}
