package com.lyubinbin.interceptor;

import com.lyubinbin.dao.TicketDAO;
import com.lyubinbin.dao.UserDAO;
import com.lyubinbin.model.Hostholder;
import com.lyubinbin.model.LoginTicket;
import com.lyubinbin.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * passport interceptor, @Component to initialize at start
 * Created by Lyu binbin on 2016/7/25.
 */
@Component
public class PassportInterceptor implements HandlerInterceptor{
    @Autowired
    TicketDAO ticketDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    Hostholder hostholder;

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostholder.clear();
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//        System.out.println("Do PassportInterceptor!");
        String ticket = null;
        if(httpServletRequest.getCookies() != null){
            for(Cookie cookie : httpServletRequest.getCookies()){
                if(cookie.getName().equals("loginTicket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
//        System.out.println(ticket);
        if(ticket != null){
            LoginTicket loginTicket = ticketDAO.selectByTicket(ticket);
            if(loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0){
                System.out.println("Invalid ticket");
                return true;
            }
            User user = userDAO.selectById(loginTicket.getUserId());
//            System.out.println("User set!");
            hostholder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null && hostholder.getUser() != null){
            modelAndView.addObject("user", hostholder.getUser());
        }
    }
}
