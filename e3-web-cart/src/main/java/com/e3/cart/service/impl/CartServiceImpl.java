package com.e3.cart.service.impl;

import com.e3.HttpClientUtil;
import com.e3.cart.service.CartService;
import com.e3.jedis.dao.JedisClient;
import com.e3.service.goods.pojo.TbItem;
import com.e3.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/22 0022.
 */
@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private JedisClient  jedisClient;


    @Override
    public void addCart(long userid, long itemId, int num) {
        //判断,redis库中是否有该商品,
        Boolean hexists = jedisClient.hexists("CART_REDIS:" + userid, itemId + "");
        // 如果有该商品,只修改商品数量,
        if (hexists){
            //取出该商品,把数量改变
            String jsondata= jedisClient.hget("CART_REDIS:" + userid, itemId + "");
            //转换商品数据
            TbItem  tbItem = JsonUtils.jsonToPojo(jsondata,TbItem.class);
            tbItem.setNum(tbItem.getNum()+num);//修改商品数量
            //写入redis中
            jedisClient.hset("CART_REDIS:" + userid, itemId + "",JsonUtils.objectToJson(tbItem));
        }
        //没有的话,去查询出该商品信息,进行添加
        //取商品详情,  调用dubbo服务.
        //8085    通过 跨域方式调用  web-manager工程中的Controller里的方法.8080
        //http://localhost:8080/item/
        String  jsondata = HttpClientUtil.doGet("http://localhost:8080/item/"+itemId);//实现跨域调用
        //数据类型转换
        jedisClient.hset("CART_REDIS:" + userid, itemId + "",jsondata);
    }

    //合并购物车
    @Override
    public void megerCart(Long userid, List<TbItem> list) {
        for (TbItem item: list){
            addCart(userid,item.getId(),item.getNum());
        }
    }

    //展示购物车信息
    @Override
    public List<TbItem> showCatList(Long userid) {
        List<String> hvals = jedisClient.hvals("CART_REDIS:" + userid);
        List<TbItem>  list = new ArrayList<>();
        for (String vals:hvals){
            TbItem tbItem = JsonUtils.jsonToPojo(vals,TbItem.class);
            list.add(tbItem);
        }

        return list;
    }


    //删除信息
    @Override
    public void delCart(long userid,long itemId) {
        //判断,redis库中是否有该商品,
        Boolean hexists = jedisClient.hexists("CART_REDIS:" + userid, itemId + "");
        // 如果有该商品,删除
        if (hexists){
            jedisClient.hdel("CART_REDIS:" + userid, itemId + "");
        }
    }
    //修改信息
    @Override
    public void updateCart(long userid,long itemId,Integer num) {
        //判断,redis库中是否有该商品,
        Boolean hexists = jedisClient.hexists("CART_REDIS:" + userid, itemId + "");
        // 如果有该商品,修改
        // 如果有该商品,只修改商品数量,
        if (hexists){
            //取出该商品,把数量改变
            String jsondata= jedisClient.hget("CART_REDIS:" + userid, itemId + "");
            //转换商品数据
            TbItem tbItem = JsonUtils.jsonToPojo(jsondata,TbItem.class);
            tbItem.setNum(num);//修改商品数量
            //写入redis中
            jedisClient.hset("CART_REDIS:" + userid, itemId + "",JsonUtils.objectToJson(tbItem));
        }
    }
}
