package com.lyubinbin;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

/**
 * Created by Lyu binbin on 2016/7/19.
 */
public class JedisTest {
    public static void print(int index, Object obj){
        System.out.println(String.format("%d: %s", index, obj.toString()));
    }

    public static void main(String[] argv) {
        //make a Jedis handler, set host, port in Jedis definition
        Jedis jedis = new Jedis();
        //delete all datas in DB, just for demo
        jedis.flushAll();

        jedis.set("hello", "world");
        jedis.rename("hello", "newhello");
        print(1, jedis.get("newhello"));

        //set expire time(seconds)
        jedis.setex("hi", 15, "world");

        jedis.set("pv", "100");
        jedis.incr("pv");
        print(2, jedis.get("pv"));
        jedis.incrBy("pv", 5);
        print(2, jedis.get("pv"));

        //list
        String listName = "list";
        for(int i = 0; i < 10; i++){
            jedis.lpush(listName, "a" + String.valueOf(i));
        }
        print(3, jedis.lrange(listName, 0, 12));
        print(4, jedis.llen(listName));
        print(5, jedis.lpop(listName));
        print(4, jedis.llen(listName));
        print(6, jedis.lindex(listName, 3));
        jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "YY");
        jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4", "XX");
        print(7, jedis.lrange(listName, 0, 12));

        //hash: convenient for adding attributes
        String userKey = "userxx";
        jedis.hset(userKey, "name", "Jim");
        jedis.hset(userKey, "age", "23");
        jedis.hset(userKey, "phone", "63273125");

        print(8, jedis.hget(userKey, "name"));
        print(8, jedis.hgetAll(userKey));
        jedis.hdel(userKey, "phone");
        print(8, jedis.hgetAll(userKey));
        print(9, jedis.hkeys(userKey));
        print(9, jedis.hvals(userKey));
        print(10, jedis.hexists(userKey, "email"));
        jedis.hsetnx(userKey, "school", "pku");
        jedis.hsetnx(userKey, "phone", "123456");
        print(8, jedis.hgetAll(userKey));

        //set
        String likeKeys1 = "newLike1";
        String likeKeys2 = "newLike2";
        for(int i = 0; i < 10; i++){
            jedis.sadd(likeKeys1, String.valueOf(i));
            jedis.sadd(likeKeys2, String.valueOf(2 * i));
        }
        print(11, jedis.smembers(likeKeys1));
        print(11, jedis.smembers(likeKeys2));
        print(12, jedis.sinter(likeKeys1, likeKeys2));
        print(12, jedis.sunion(likeKeys1, likeKeys2));
        print(12, jedis.sdiff(likeKeys1, likeKeys2));
        print(13, jedis.sismember(likeKeys1, "5"));
        jedis.srem(likeKeys1, "5");
        print(13, jedis.sismember(likeKeys1, "5"));
        print(13, jedis.smembers(likeKeys1));
        print(14, jedis.scard(likeKeys1));
        jedis.smove(likeKeys2, likeKeys1, "14");
        print(15, jedis.smembers(likeKeys1));

        //sorted set(start with z), for ranking
        String rankKey = "ranKey";
        jedis.zadd(rankKey, 15, "Jim");
        jedis.zadd(rankKey, 60, "Ben");
        jedis.zadd(rankKey, 90, "Mei");
        jedis.zadd(rankKey, 100, "Lyu");
        jedis.zadd(rankKey, 35, "Bee");
        print(16, jedis.zcard(rankKey));
        print(16, jedis.zcount(rankKey, 61, 100));
        print(16, jedis.zscore(rankKey, "Lyu"));
        jedis.zincrby(rankKey, 2, "Ben");
        print(17,jedis.zrange(rankKey, 0, 2));
        print(17,jedis.zrevrange(rankKey, 0, 2));
        for(Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "0", "100")){
            print(18, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }
        print(19, jedis.zrank(rankKey, "Ben"));
        print(19, jedis.zrevrank(rankKey, "Ben"));

        JedisPool pool = new JedisPool();
        for(int i = 0; i < 100; i++){
            Jedis j = pool.getResource();
            j.get("a");
            System.out.println("POOL" + i);
            j.close();
        }
    }
}
