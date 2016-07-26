package com.lyubinbin.async.handler;


import com.lyubinbin.async.EventHandler;
import com.lyubinbin.async.EventModel;
import com.lyubinbin.async.EventType;
import com.lyubinbin.model.Message;
import com.lyubinbin.service.MessageService;
import com.lyubinbin.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * send an email when login
 * Created by Lyu binbin on 2016/7/20.
 */
@Component
public class LoginExceptionHandler implements EventHandler{

    @Autowired
    MessageService messageService;
    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        //jedge if abnormal login happen
        System.out.println("Do LoginExceptionHandler!");
        Message message = new Message();
        message.setToId(model.getActorId());
        message.setFromId(3);
        message.setCreatedDate(new Date());
        messageService.addMessage(message);

        Map<String, Object> map = new HashMap<>();
        map.put("username", model.getExts("username"));
        mailSender.sendWithHTMLTemplate(model.getExts("email"), "Welcome to Toutiao", "mails/welcome.html", map);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LOGIN);
    }
}