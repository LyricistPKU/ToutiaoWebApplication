package com.lyubinbin.service;

import com.lyubinbin.dao.MessageDAO;
import com.lyubinbin.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lyu binbin on 2016/7/25.
 */
@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    public int addMessage(Message message){
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit){
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit){
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public int getConversationUnreadCount(int userId, String conversationId){
        return messageDAO.getConversationUnReadCount(userId, conversationId);
    }

    public int getConversationCount(String conversationId){
        return messageDAO.getConversationCount(conversationId);
    }

    public int updateMessageStatus(String conversationId, int toId, int hasRead){
        return messageDAO.updateMessageStatus(conversationId, toId, hasRead);
    }
}
