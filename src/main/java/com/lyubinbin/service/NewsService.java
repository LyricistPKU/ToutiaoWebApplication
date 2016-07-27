package com.lyubinbin.service;

import com.lyubinbin.dao.NewsDAO;
import com.lyubinbin.model.News;
import com.lyubinbin.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * save image in local place, just for test
 * Created by Lyu binbin on 2016/7/25.
 */
@Service
public class NewsService {
    @Autowired
    NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset, int limit){
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public List<News> getGolobalLatestNews(int offset, int limit){
        return newsDAO.selectByOffset(offset, limit);
    }

    public int addNews(News news){
        newsDAO.addNews(news);
        return news.getId();
    }

    public News getById(int newsId){
        return newsDAO.selectById(newsId);
    }

    // save image in local PC, save in Qiniu in real situation
    public String saveIamge(MultipartFile file) throws IOException{
        int dotpos = file.getOriginalFilename().lastIndexOf(".");
        if(dotpos < 0){
            return null;
        }

        String fileExt = file.getOriginalFilename().substring(dotpos + 1).toLowerCase();
        if(!ToutiaoUtil.isFileUploadAlloewed(fileExt)){
            return null;
        }

        String filename = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        Files.copy(file.getInputStream(), new File(ToutiaoUtil.IMAGE_DIR + filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + filename;
    }

    public int updateCommentCount(int id, int count) {
        return newsDAO.updateCommentCount(id, count);
    }

    public int updateLikeCount(int id, int count) {
        return newsDAO.updateLikeCount(id, count);
    }
}
