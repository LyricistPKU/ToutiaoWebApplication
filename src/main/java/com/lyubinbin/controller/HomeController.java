package com.lyubinbin.controller;

import com.lyubinbin.model.EntityType;
import com.lyubinbin.model.Hostholder;
import com.lyubinbin.model.News;
import com.lyubinbin.model.ViewObject;
import com.lyubinbin.service.LikeService;
import com.lyubinbin.service.NewsService;
import com.lyubinbin.service.UserService;
import com.lyubinbin.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * home page
 * Created by Lyu binbin on 2016/7/25.
 */
@Controller
public class HomeController {
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

    // for return news list viewobject
    private List<ViewObject> getNews(int userId, int offset, int limit){
        List<News> newsList = newsService.getLatestNews(userId, offset, limit);
        int localUserId = hostholder.getUser() == null ? 0 : hostholder.getUser().getId();
        List<ViewObject> vos = new ArrayList<>();
        for(News news : newsList){
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUserId()));
            if(localUserId != 0){
                vo.set("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            }
            else{
                vo.set("like", 0);
            }
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String index(Model model, @RequestParam(value = "pop", defaultValue = "0") int pop){
        model.addAttribute("vos", getNews(0, 0, 10));
        if(hostholder.getUser() != null){
            pop = 0;
        }
        model.addAttribute("pop", pop);
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String userIndex(Model model, @PathVariable("userId") int userId){
        model.addAttribute("vos", getNews(userId, 0, 10));
        return "home";
    }
}
