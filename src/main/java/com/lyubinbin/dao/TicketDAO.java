package com.lyubinbin.dao;

import com.lyubinbin.model.LoginTicket;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * Use @Mapper to connect dao object to database
 * Created by Lyu binbin on 2016/7/25.
 */

@Mapper
public interface TicketDAO {
    String TABLE_NAME = "login_ticket";

    String INSERT_FIELDS = " user_id, expired, status, ticket ";

    String SELECT_FIELDS = " id, user_id, expired, status, ticket ";

    @Insert({
            "insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") Values (#{userId}, #{expired}, #{status}, #{ticket})"
    })
    int addTicket(LoginTicket loginTicket);

    @Select({
            "select ", SELECT_FIELDS, " from ", TABLE_NAME, "where ticket = #{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    @Select({
            "select ", SELECT_FIELDS, " from ", TABLE_NAME, "where user_id = #{userId}"
    })
    LoginTicket selectByUserId(int userId);

    @Update({
            "update ", TABLE_NAME, "set status = #{status} where ticket = #{ticket}"
    })
    void upadateStatus(@Param("ticket") String ticket, @Param("status") int status);

    @Update({
            "update ", TABLE_NAME, "set expired = #{expired} where ticket = #{ticket}"
    })
    void upadateExpired(@Param("ticket") String ticket, @Param("expired") Date expired);
}
