package com.e3.item.listener;

import com.e3.service.goods.api.ItemService;
import com.e3.service.goods.pojo.TbItem;
import com.e3.service.goods.pojo.TbItemDesc;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CYJ on 2018/3/19.
 */
public class ItemListener implements MessageListener {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private ItemService itemService;

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
           // searchService.sysnIndexSearch(itemId);
            // 1、从spring容器中获得FreeMarkerConfigurer对象。
            // 2、从FreeMarkerConfigurer对象中获得Configuration对象。
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            // 3、使用Configuration对象获得Template对象。
            Template template = configuration.getTemplate("item.ftl");
            // 4、创建数据集
            Map dataModel = new HashMap<>();

            //商品基本信息
            TbItem tbItem =itemService.queryByItemId(itemId);
            //商品详情
            TbItemDesc descs=itemService.queryByItemIdDesc(itemId);
            dataModel.put("item",tbItem);

            dataModel.put("itemDesc",descs);
            // 5、创建输出文件的Writer对象。
            Writer out = new FileWriter(new File("F:/temp/"+itemId+".html"));
            // 6、调用模板对象的process方法，生成文件。
            template.process(dataModel, out);
            // 7、关闭流。
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
