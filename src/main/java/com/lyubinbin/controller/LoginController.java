package com.lyubinbin.controller;

import com.lyubinbin.async.EventModel;
import com.lyubinbin.async.EventProducer;
import com.lyubinbin.async.EventType;
import com.lyubinbin.service.UserService;
import com.lyubinbin.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * login controller
 * Created by Lyu binbin on 2016/7/25.
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/reg"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int rememberMe,
                      HttpServletResponse response){
        try{
            Map<String, Object> map = userService.register(username, password);
            if(map.containsKey("loginTicket")){
                Cookie cookie = new Cookie("loginTicket", map.get("loginTicket").toString());
                cookie.setPath("/");
                // remember me selection in login form in html page
                if(rememberMe > 0){
                    cookie.setMaxAge(3600 * 24 * 7);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "Register Succeeded!");
            }
            else{
                return ToutiaoUtil.getJSONString(1, map);
            }
        }catch (Exception e){
            logger.error("Regist failed" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "Regist failed");
        }
    }

    @RequestMapping(path = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int rememberMe,
                      HttpServletResponse response){
        try{
            Map<String, Object> map = userService.login(username, password);
            if(map.containsKey("loginTicket")){
                Cookie cookie = new Cookie("loginTicket", map.get("loginTicket").toString());
                cookie.setPath("/");

                if(rememberMe > 0){
                    cookie.setMaxAge(3600 * 24 * 7);
                }
                response.addCookie(cookie);

                Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
                if(p.matcher(username).matches()){
                    eventProducer.fireEvent(new EventModel(EventType.LOGIN).setActorId((int) map.get("userId"))
                            .setExts("username", username).setExts("email", username));                    
                }
                return ToutiaoUtil.getJSONString(0, "Login Succeeded!");
            }
            else{
                return ToutiaoUtil.getJSONString(1, map);
            }
        }catch (Exception e){
            logger.error("Login failed: " + e.getMessage());
            e.printStackTrace();
            return ToutiaoUtil.getJSONString(1, "Login failed");
        }
    }

    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("loginTicket") String loginTicket){
        userService.logout(loginTicket);
        return "redirect:/";
    }
}
