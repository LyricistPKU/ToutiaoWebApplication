package com.lyubinbin.dao;

import com.lyubinbin.model.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Use @Mapper to connect dao object to database
 * Created by Lyu binbin on 2016/7/25.
 */
@Mapper
public interface UserDAO {
    String TABLE_NAME = "user";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, name, password, salt, head_url ";

    @Insert({
            "insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") Values (#{name}, #{password}, #{salt}, #{headUrl})"
    })
    int addUser(User user);

    @Select({
            "select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id = #{id}"
    })
    User selectById(int id);

    @Select({
            "select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name = #{name}"
    })
    User selectByName(String name);

    @Select({
            "update ", TABLE_NAME, " set password = #{password} where id = #{id}"
    })
    void updatePassword(User user);

    @Delete({
            "delete from ", TABLE_NAME, "where id = #{id}"
    })
    void deleteById(int id);
}
