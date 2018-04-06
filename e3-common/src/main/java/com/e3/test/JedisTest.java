package com.e3.test;

import redis.clients.jedis.Jedis;

/**
 * Created by CYJ on 2018/3/12.
 */
public class JedisTest {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.177.128", 6379);
        jedis.set("title","asda");
        System.out.print(jedis.get("title"));


    }

}
