package com.e3.order.service.impl;

import com.e3.HttpClientUtil;
import com.e3.jedis.dao.JedisClient;
import com.e3.order.service.OrderServices;
import com.e3.service.goods.pojo.TbItem;
import com.e3.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
@Service
public class OrderServiceImpl implements OrderServices {
    @Autowired
    private JedisClient jedisClient;

    @Override
    public void addCart(long userid, long itemid, Integer num) {
        //1.判断redis中是否有此数据
        Boolean hexists = jedisClient.hexists("CART_REDIS:" + userid, itemid + "");
        //如果有该商品
        if(hexists){
            //把商品信息取出,重新设置商品的数量,再写入到redis中
            String jsondata = jedisClient.hget("CART_REDIS:" + userid, itemid + "");
            //类型转换
            TbItem tbItem = JsonUtils.jsonToPojo(jsondata, TbItem.class);
            tbItem.setNum(num);//设置数量.
            //写入到redis中
            jedisClient.hset("CART_REDIS:" + userid, itemid + "",JsonUtils.objectToJson(tbItem));
        }
        //redis库中没有该条商品信息,
        //1.根据id从数据库中查询商品信息
        String jsondata =  HttpClientUtil.doGet("http://localhost:8080/e3-web-manager/item/"+itemid);
        TbItem tbItem = JsonUtils.jsonToPojo(jsondata,TbItem.class);
        //2.把商品信息,写入到redis中.
        jedisClient.hset("CART_REDIS:" + userid, itemid + "",JsonUtils.objectToJson(tbItem));
    }
    //合并购物车
    @Override
    public void megareCart(long userid, List<TbItem> list) {
        //1.判断cookie中是否有该商品
        //2如果有修改数量
        //3.写入到redis中
        for (TbItem items:list){
            addCart(userid,items.getId(), items.getNum());
        }
    }
    //展示购物车信息
    @Override
    public List<TbItem> showCart(long userid) {
        //从redis中获取数据
        List<String> list = jedisClient.hvals("CART_REDIS:" + userid);
        List<TbItem>  result = new ArrayList<>();
        for(String s: list){
            TbItem  tbItem = new TbItem();
            result.add(JsonUtils.jsonToPojo(s,TbItem.class));//类型转换
        }
        return result;
    }
}
