package com.lyubinbin;

import com.lyubinbin.dao.CommentDAO;
import com.lyubinbin.dao.MessageDAO;
import com.lyubinbin.model.Comment;
import com.lyubinbin.model.EntityType;
import com.lyubinbin.model.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.security.PrivateKey;
import java.util.Date;

/**
 * Created by Lyu binbin on 2016/7/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = ToutiaoNewsApplication.class)
//@WebAppConfiguration  这行会修改默认的启动路径需要注释掉
public class InitCommentTests {
    @Autowired
    CommentDAO commentDAO;
    @Autowired
    MessageDAO messageDAO;

    @Test
    public void initCommentMessage(){
        for(int i = 0; i < 10; i++){
            Comment comment = new Comment();
            comment.setEntityId(1);
            comment.setUserId(i + 1);
            comment.setStatus(0);
            comment.setcreatedDate(new Date());
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setContent("Test Comment:" + String.valueOf(i));
            commentDAO.addComment(comment);

            Message message = new Message();
            message.setContent("Test Comment:" + String.valueOf(i));
            message.setFromId(i + 1);
            message.setToId(12);
            message.setHasRead(0);
            String conversation_id = message.getFromId() < message.getToId() ? String.valueOf(message.getFromId()) + "_" + String.valueOf(message.getToId()) :
                    String.valueOf(message.getToId()) + "_" + String.valueOf(message.getFromId());
            message.setConversationId(conversation_id);
            message.setCreatedDate(new Date());
            messageDAO.addMessage(message);
        }
    }
}
