package com.lyubinbin.service;

import com.lyubinbin.dao.TicketDAO;
import com.lyubinbin.dao.UserDAO;
import com.lyubinbin.model.LoginTicket;
import com.lyubinbin.model.User;
import com.lyubinbin.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * user login/logout/ticket manipulation
 * Created by Lyu binbin on 2016/7/25.
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    TicketDAO ticketDAO;

    public Map<String, Object> register(String username, String password){
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msg1", "User name can't be empty!");
            return map;
        }
        if(StringUtils.isBlank((password))){
            map.put("msg2", "Password can't be empty!");
            return map;
        }
        // search if the username is already exist
        if(userDAO.selectByName(username) != null){
            map.put("msg3", "The user name has already existed");
            return map;
        }

        User user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(ToutiaoUtil.MD5(password + user.getSalt()));
        userDAO.addUser(user);

        LoginTicket loginTicket = addLoginTicket(user.getId());
        map.put("loginTicket", loginTicket.getTicket());
        return map;
    }

    public Map<String, Object> login(String username, String password){
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msg1", "User name can't be empty!");
            return map;
        }
        if(StringUtils.isBlank((password))){
            map.put("msg2", "Password can't be empty!");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user == null){
            map.put("msg3", "User name doesn't exist.");
            return map;
        }
        if(!ToutiaoUtil.MD5(password + user.getSalt()).equals(user.getPassword())){
            map.put("msg4", "Password inCorrect.");
        }

        map.put("userId", user.getId());

        LoginTicket loginTicket;
        if(ticketDAO.selectByUserId(user.getId()) != null){
            loginTicket = ticketDAO.selectByUserId(user.getId());
            // make invalid cookie valid
            if(loginTicket.getStatus() == 1){
                ticketDAO.upadateStatus(loginTicket.getTicket(), 0);
            }
            if(loginTicket.getExpired().before(new Date())){
                Date date = new Date();
                date.setTime(date.getTime() + 1000 * 3600 * 24);
                ticketDAO.upadateExpired(loginTicket.getTicket(), date);
            }
        }
        else{
            loginTicket = addLoginTicket(user.getId());
        }

        map.put("loginTicket", loginTicket.getTicket());
        return map;
    }

    private LoginTicket addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-", ""));
        ticketDAO.addTicket(loginTicket);
        return loginTicket;
    }

    public void logout(String loginTicket){
        ticketDAO.upadateStatus(loginTicket, 1);
    }

    public User getUser(int id){
        return userDAO.selectById(id);
    }
}
