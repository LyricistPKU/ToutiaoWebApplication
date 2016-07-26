package com.lyubinbin.async;

import java.util.List;

/**
 * Created by Lyu binbin on 2016/7/20.
 */
public interface EventHandler {
    void doHandle(EventModel model);
    //what types of event should handler focus on
    List<EventType> getSupportEventType();
}
