package com.lyubinbin.model;
import java.util.Date;

/**
 * news model
 * Created by Lyu binbin on 2016/7/3.
 */

public class News {

    private int id;     // news id

    private String title;   // news title

    private String link;    // news link

    private String image;   // news image url, stored in qiniu cloud server

    private int likeCount;  // amount of like

    private int commentCount;   // amount of comment

    private Date createdDate;   // create date

    private int userId;     // owner user id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
