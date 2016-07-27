package com.lyubinbin.controller;

import com.lyubinbin.model.*;
import com.lyubinbin.service.LikeService;
import com.lyubinbin.service.MessageService;
import com.lyubinbin.service.NewsService;
import com.lyubinbin.service.UserService;
import com.lyubinbin.util.MailSender;
import com.lyubinbin.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * home page
 * Created by Lyu binbin on 2016/7/25.
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    LikeService likeService;
    @Autowired
    Hostholder hostholder;
    @Autowired
    MailSender mailSender;
    @Autowired
    MessageService messageService;

    // for return news list viewobject
    private List<ViewObject> getNews(int userId, int offset, int limit){
        int localUserId = hostholder.getUser() == null ? 1 : hostholder.getUser().getId();
        List<ViewObject> vos = new ArrayList<>();
        List<News> newsList = newsService.getLatestNews(userId, offset, limit);
        for(News news : newsList){
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUserId()));
            if(localUserId != 1){
                vo.set("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            }
            else{
                vo.set("like", 0);
            }
            vos.add(vo);
        }
        return vos;
    }

    private List<ViewObject> getLastesNews(int offset, int limit){
        List<News> newsList = newsService.getGolobalLatestNews(offset, limit);
        int localUserId = hostholder.getUser() == null ? 1 : hostholder.getUser().getId();
        List<ViewObject> vos = new ArrayList<>();
        for(News news : newsList){
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUserId()));
            if(localUserId == 1){
                vo.set("like", 0);
            }
            else{
                vo.set("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            }
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String index(Model model, @RequestParam(value = "pop", defaultValue = "0") int pop){
        int userId = hostholder.getUser() == null ? 1 : hostholder.getUser().getId();
//        System.out.println(userId);
        if(hostholder.getUser() != null){
            pop = 0;
        }
        model.addAttribute("pop", pop);
        //get latest 20 news
        model.addAttribute("vos", getLastesNews(0, 20));
        model.addAttribute("user", hostholder.getUser());
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String userIndex(Model model, @PathVariable("userId") int userId){
        //get latest 10 news of a user
        model.addAttribute("vos", getNews(userId, 0, 10));
        model.addAttribute("user", hostholder.getUser());
        model.addAttribute("target", userService.getUser(userId));
        return "user";
    }

    // add message and redirect to the same page
    @RequestMapping(path = {"/addMessage"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content){
        System.out.println("Doing add mssage");
        try{
            content = HtmlUtils.htmlEscape(content);
            if(fromId != toId && content != null){
                Message message = new Message();
                message.setFromId(fromId);
                message.setToId(toId);
                message.setContent(content);
                message.setCreatedDate(new Date());
                message.setHasRead(0);
                message.setConversationId(ToutiaoUtil.getConversationId(fromId, toId));
                messageService.addMessage(message);
            }
        }catch (Exception e){
            logger.error("Add Message Failed: " + e.getMessage());
        }
        return "redirect:/user/" + String.valueOf(toId);
    }
}
