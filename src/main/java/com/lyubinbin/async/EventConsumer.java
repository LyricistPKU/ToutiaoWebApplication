package com.lyubinbin.async;

import com.alibaba.fastjson.JSON;
import com.lyubinbin.util.JedisAdapter;
import com.lyubinbin.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * event consumer using redis brpop
 * Created by Lyu binbin on 2016/7/20.
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware{
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    //map one event to many handlers
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private  ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    //after initializing
    @Override
    public void afterPropertiesSet() throws Exception {
        //find all classes that implements EventHandler
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if(beans != null){
            for(Map.Entry<String, EventHandler> entry : beans.entrySet()){
                List<EventType> eventTypes = entry.getValue().getSupportEventType();
                for(EventType type : eventTypes){
                    if(!config.containsKey(type)){
                        config.put(type, new ArrayList<>());
                    }
                    System.out.println(type.toString() + "/" + entry.getValue().toString());
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String key = RedisKeyUtil.getEventQueueKey();
                while(true){
                    // get evets from the right side of the list, blocking
                    List<String> events = jedisAdapter.brpop(0, key);

                    // brpop returns key name and value, you should ignore the key string
                    for(String message : events){
//                      System.out.println("Event Consumer Started!");
                        if(message.equals(key)){
                            continue;
                        }
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        if(!config.containsKey(eventModel.getType())){
                            logger.error("Unrecognizable event!");
                            continue;
                        }
                        for(EventHandler handler : config.get(eventModel.getType())){
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
