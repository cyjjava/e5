package com.e3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Created by CYJ on 2018/3/5.
 */
public class SearchServiceProvider {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(SearchServiceProvider.class);
    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
            context.start();
        } catch (Exception e) {
            LOGGER.error("== DubboProvider context start error:",e);
        }
        synchronized (SearchServiceProvider.class) {
            while (true) {
                try {
                    SearchServiceProvider.class.wait();
                    System.out.println("商品服务已启动成功！");
                } catch (InterruptedException e) {
                    LOGGER.error("== synchronized error:",e);
                }
            }
        }
    }
}
