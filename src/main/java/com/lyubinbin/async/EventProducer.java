package com.lyubinbin.async;

import com.alibaba.fastjson.JSONObject;
import com.lyubinbin.util.JedisAdapter;
import com.lyubinbin.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Lyu binbin on 2016/7/20.
 */
@Service
public class EventProducer {
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel model){
        try{
            String json = JSONObject.toJSONString(model);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            System.out.println(key + " : " + json);
            return true;
        }catch (Exception e){
            logger.error("Fire Event Error: " + e.getMessage());
            return false;
        }
    }
}
