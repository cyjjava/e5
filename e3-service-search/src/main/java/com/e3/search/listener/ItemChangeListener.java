package com.e3.search.listener;

import com.e3.service.search.api.SearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by Administrator on 2018/1/2 0002.
 */
public class ItemChangeListener  implements MessageListener {
    @Autowired
    private SearchService searchService;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = null;
            Long itemId = null;
            //取商品id
            if (message instanceof TextMessage) {
                textMessage = (TextMessage) message;
                itemId = Long.parseLong(textMessage.getText());
            }
            //等待1秒钟以后再往索引库中添加
            Thread.sleep(1000);
            //向索引库添加文档
            searchService.sysnIndexSearch(itemId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}