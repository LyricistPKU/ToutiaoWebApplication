package com.lyubinbin;

import com.lyubinbin.dao.NewsDAO;
import com.lyubinbin.dao.TicketDAO;
import com.lyubinbin.dao.UserDAO;
import com.lyubinbin.model.LoginTicket;
import com.lyubinbin.model.News;
import com.lyubinbin.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

//Junit for test
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoNewsApplication.class)
//@WebAppConfiguration  这行会修改默认的启动路径需要注释掉
//@Sql({"/init-schema.sql"})
public class InitDatabaseTests {
    @Autowired
    NewsDAO newsDAO;
    @Autowired
    TicketDAO ticketDAO;
    @Autowired
    UserDAO userDAO;

    @Test
    public void InitDataBase() {
        Random r = new Random();

        for (int i = 0; i < 11; ++i) {
            // random users
            User user = new User();
            user.setName("admin");
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", r.nextInt(1000)));
            user.setPassword("123456");
            user.setSalt("654321");
            userDAO.addUser(user);

            // random news
//            News news = new News();
//            news.setCommentCount(i);
//            Date date = new Date();
//            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
//            news.setCreatedDate(date);
//            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", r.nextInt(1000)));
//            news.setLikeCount(i + 1);
//            news.setLink(String.format("http://www.nowcoder.com/link/{%d}.html", i));
//            news.setTitle(String.format("Title {%d} ", i));
//            news.setUserId(i+1);
//            newsDAO.addNews(news);

//            user.setPassword("newpassword");
//            userDAO.updatePassword(user);
        }

//        for(int i = 0; i < 10; i++){
//            LoginTicket ticket = new LoginTicket();
//            ticket.setStatus(i % 2);
//            Date date = new Date();
//            date.setTime(date.getTime()+100*3600*24);
//            ticket.setExpired(date);
//            ticket.setTicket("hello"+i);
//            ticket.setUserId(100+i);
//            ticketDAO.addTicket(ticket);
//        }

//        Assert.assertEquals("newpassword", userDAO.selectById(1).getPassword());
//        userDAO.deleteById(1);
//        Assert.assertNotNull(userDAO.selectById(1));
    }
}
