package com.lyubinbin.controller;


import com.lyubinbin.model.Hostholder;
import com.lyubinbin.model.Message;
import com.lyubinbin.model.User;
import com.lyubinbin.model.ViewObject;
import com.lyubinbin.service.MessageService;
import com.lyubinbin.service.UserService;
import com.lyubinbin.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lyu binbin on 2016/7/15.
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    Hostholder hostholder;

    @RequestMapping(path = {"/msg/addmessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addComment(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content){
        try{
            Message msg = new Message();
            msg.setContent(content);
            msg.setFromId(fromId);
            msg.setToId(toId);
            msg.setCreatedDate(new Date());
            msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            messageService.addMessage(msg);
            return ToutiaoUtil.getJSONString(0, msg.getConversationId());
        }catch (Exception e){
            logger.error("Add measage failed: " + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "add comment failed");
        }
    }

    // conversation detail
    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(@Param("conversationId") String conversationId, Model model){
        try{
            if(hostholder.getUser() != null){
                messageService.updateMessageStatus(conversationId, hostholder.getUser().getId(), 1);
            }
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messages = new ArrayList<>();
//            System.out.println(conversationList.size());
            for(Message msg : conversationList){
                System.out.println(msg.toString());
                messageService.updateMessageStatus(conversationId, msg.getToId(), 1);
                ViewObject vo = new ViewObject();
                vo.set("message", msg);
                User user = userService.getUser(msg.getFromId());
                if(user == null){
                    continue;
                }
                model.addAttribute("user", user);
                vo.set("headUrl", user.getHeadUrl());
                if(hostholder.getUser() != null && hostholder.getUser().getId() == user.getId()){
                    vo.set("name", "Me");
                }
                else{
                    vo.set("name", user.getName());
                }
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
            return "letterDetail";
        }catch (Exception e){
            logger.error("Get Conversation Detail Failed: " + e.getMessage());
            e.printStackTrace();
            return ToutiaoUtil.getJSONString(1, "Get Conversation Detail Failed");
        }
    }

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationList(Model model){
        try{
            int localUserId = hostholder.getUser() == null ? 1 : hostholder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            for(Message msg : conversationList){
//                System.out.println(msg.getContent());
                ViewObject vo = new ViewObject();
                vo.set("msg", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("target", user);
                vo.set("unread", messageService.getConversationUnreadCount(localUserId, msg.getConversationId()));
                vo.set("count", messageService.getConversationCount(msg.getConversationId()));
                vo.set("conversationId", ToutiaoUtil.getConversationId(msg.getFromId(), msg.getToId()));
                conversations.add(vo);
            }
//            System.out.println(localUserId);
            model.addAttribute("user", hostholder.getUser());
            model.addAttribute("conversation", conversations);
        }catch (Exception e){
            logger.error("Reading Conversation List Failed: " + e.getMessage());
        }
        return "letter";
    }
}
