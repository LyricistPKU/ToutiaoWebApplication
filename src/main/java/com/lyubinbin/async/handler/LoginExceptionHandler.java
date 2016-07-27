package com.lyubinbin.async.handler;


import com.lyubinbin.async.EventHandler;
import com.lyubinbin.async.EventModel;
import com.lyubinbin.async.EventType;
import com.lyubinbin.model.Message;
import com.lyubinbin.service.MessageService;
import com.lyubinbin.util.MailSender;
import com.lyubinbin.util.ToutiaoUtil;
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
        // user id 0 is administry account
//        System.out.println("Do LoginExceptionHandler!");
        if(model.getActorId() != 1){
            Message message = new Message();
            message.setToId(model.getActorId());
            message.setFromId(1);
            message.setCreatedDate(new Date());
            message.setConversationId(ToutiaoUtil.getConversationId(1, model.getActorId()));
            message.setHasRead(0);
            message.setContent("You logged in ToutiaoNews at: " + new Date().toString());
            messageService.addMessage(message);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("username", model.getExts("username"));
        mailSender.sendWithHTMLTemplate(model.getExts("email"), "Welcome to Toutiao", "mails/welcome.html", map);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LOGIN);
    }
}
