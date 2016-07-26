package com.lyubinbin.controller;


import com.lyubinbin.async.EventModel;
import com.lyubinbin.async.EventProducer;
import com.lyubinbin.async.EventType;
import com.lyubinbin.model.EntityType;
import com.lyubinbin.model.Hostholder;
import com.lyubinbin.model.News;
import com.lyubinbin.service.LikeService;
import com.lyubinbin.service.NewsService;
import com.lyubinbin.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Lyu binbin on 2016/7/19.
 */
@Controller
public class LikeController {
    @Autowired
    Hostholder hostholder;
    @Autowired
    LikeService likeService;
    @Autowired
    NewsService newsService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId){
        int userId = hostholder.getUser().getId();
        News news = newsService.getById(newsId);
        long likeCount = likeService.like(userId, EntityType.ENTITY_NEWS, newsId);
        // update likeCount in news DB
        newsService.updateLikeCount(newsId, (int)likeCount);
        //produce an event to send an internal message
        //the return value of set function is this, you can set all attributes in a line
        eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(hostholder.getUser().getId()).setEntityType(EntityType.ENTITY_NEWS)
        .setEntityId(newsId).setEntityOwnerId(news.getUserId()));
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("newsId") int newsId){
        int userId = hostholder.getUser().getId();
        long likeCount = likeService.dislike(userId, EntityType.ENTITY_NEWS, newsId);
        // update likeCount in news DB
        newsService.updateLikeCount(newsId, (int)likeCount);
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
