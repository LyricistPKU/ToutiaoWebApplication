package com.lyubinbin.controller;

import com.lyubinbin.model.*;
import com.lyubinbin.service.*;
import com.lyubinbin.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * news detail page
 * like or dislike, add comments, upload images
 * Created by Lyu binbin on 2016/7/25.
 */

@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;
    @Autowired
    QiniuService qiniuService;
    @Autowired
    Hostholder hostholder;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;

    //show news detail page
    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model){
        News news = newsService.getById(newsId);
        if(news != null){
            int localUserId = hostholder.getUser() == null ? 0 : hostholder.getUser().getId();
            if(localUserId != 0){
                model.addAttribute("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            }
            else{
                model.addAttribute("like", 0);
            }
            List<Comment> comments = commentService.getCommentByEntity(EntityType.ENTITY_NEWS, newsId);
            List<ViewObject> vos = new ArrayList<>();
            for(Comment comment : comments){
                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("user", userService.getUser(comment.getUserId()));
                vos.add(vo);
            }
            model.addAttribute("commentvos", vos);
        }
        model.addAttribute("news", news);
        model.addAttribute("owner", userService.getUser(news.getUserId()));
        return "detail";
    }

    // add comment and redirect to the same page
    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.GET})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content){
        try{
            // filtering content
            content = HtmlUtils.htmlEscape(content);
            Comment comment = new Comment();
            comment.setUserId(hostholder.getUser().getId());
            comment.setEntityId(newsId);
            // comment on news, you can also design comment on comments
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setContent(content);
            comment.setcreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);

            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(newsId, count);

        }catch (Exception e){
            logger.error("Add Comment Failed: " + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }

    // click to read the local or cloud image
    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String iamgeName,
                         HttpServletResponse response){
        try{
            response.setContentType("image/jpeg");
            // download from qiniu cloud ???
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.QINIU_DOMAIN_PREFIX + iamgeName)), response.getOutputStream());
        }catch (Exception e){
            logger.error("Read Image Failed: " + e.getMessage());
        }
    }

    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            //String fileUrl = newsService.saveIamge(file);
            String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null) {
                return ToutiaoUtil.getJSONString(1, "Upload Failed");
            }
            return ToutiaoUtil.getJSONString(0, fileUrl);
        } catch (Exception e) {
            logger.error("Upload Failed" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "Upload Failed");
        }
    }

    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            News news = new News();
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setImage(image);
            news.setLink(link);
            if (hostholder.getUser() != null) {
                news.setUserId(hostholder.getUser().getId());
            } else {
                // anonymous user
                news.setUserId(3);
            }
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("Adding news Failed: " + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "Adding news Failed");
        }
    }
}
