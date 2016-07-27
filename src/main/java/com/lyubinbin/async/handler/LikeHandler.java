package com.lyubinbin.async.handler;


import com.lyubinbin.async.EventHandler;
import com.lyubinbin.async.EventModel;
import com.lyubinbin.async.EventType;
import com.lyubinbin.model.Message;
import com.lyubinbin.model.User;
import com.lyubinbin.service.MessageService;
import com.lyubinbin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * put like event in asynchronous queue --- send internal message
 * Created by Lyu binbin on 2016/7/20.
 */
@Component
public class LikeHandler implements EventHandler{

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        System.out.println("Do LikeHandler!");

        Message message = new Message();
        //from id is system user id
        message.setFromId(1);
        User user = userService.getUser(model.getActorId());
        message.setContent("User " + user.getName() + "liked your news http://127.0.0.1:8080/news/" + model.getEntityId());
        message.setCreatedDate(new Date());
        message.setToId(model.getEntityOwnerId());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LIKE);
    }
}
