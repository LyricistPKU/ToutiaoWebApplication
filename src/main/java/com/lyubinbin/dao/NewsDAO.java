package com.lyubinbin.dao;

import com.lyubinbin.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Use @Mapper to connect dao object to database
 * Created by Lyu binbin on 2016/7/25.
 */

@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";

    String INSERT_FIELDS = " title, link, image, like_count, comment_count,created_date,user_id ";

    String SELECT_FIELDS = " id, title, link, image, like_count, comment_count,created_date,user_id ";

    @Insert({
            "insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") Values (#{title},#{link},#{image},#{likeCount}, #{commentCount},#{createdDate},#{userId})"       //note that you should not write like_count
    })
    int addNews(News news);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    News selectById(int id);

    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    @Update({"update ", TABLE_NAME, " set like_count = #{likeCount} where id=#{id}"})
    int updateLikeCount(@Param("id") int id, @Param("likeCount") int likeCount);

    @Select({
        "select ", SELECT_FIELDS, " from ", TABLE_NAME, " where user_id = #{userId} order by id desc limit #{offset}, #{limit}"
    })
    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

}
