package com.lyubinbin.util;

/**
 * Generate Redis Key
 * Created by Lyu binbin on 2016/7/19.
 */
public class RedisKeyUtil {
    private static  String SPLIT = ":";
    private static  String BIZ_LIKE = "LIKE";
    private static  String BIZ_DISLIKE = "DISLIKE";
    private static  String BIZ_EVENT = "EVENT";

    public static String getEventQueueKey(){
        return BIZ_EVENT;
    }

    public static String getLikeKey(int entityType, int entityId){
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDislikeKey(int entityType, int entityId){
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
}
