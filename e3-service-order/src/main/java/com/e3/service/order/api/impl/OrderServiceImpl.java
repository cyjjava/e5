package com.e3.service.order.api.impl;

import com.e3.E3Result;
import com.e3.jedis.dao.JedisClient;
import com.e3.service.order.api.OrderService;
import com.e3.service.order.mapper.TbOrderItemMapper;
import com.e3.service.order.mapper.TbOrderMapper;
import com.e3.service.order.mapper.TbOrderShippingMapper;
import com.e3.service.order.pojo.OrderInfo;
import com.e3.service.order.pojo.TbOrderItem;
import com.e3.service.order.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private TbOrderItemMapper orderItemMapper;

    @Autowired
    private TbOrderShippingMapper  shippingMapper;

    @Autowired
    private JedisClient jedisClient;

    @Transactional
    @Override
    public E3Result createOrder(OrderInfo orderInfo) {
        //订单号 1  初始化订单号
        Boolean orderflag = jedisClient.exists("ORDER_ID");
        if (!orderflag){//初始化订单号
            jedisClient.set("ORDER_ID","100544");
        }
        long orderId = jedisClient.incr("ORDER_ID");//自增订单号

        orderInfo.setOrderId(orderId+"");
        orderInfo.setStatus(1);//1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭',
        orderInfo.setCreateTime(new Date());
        orderMapper.insert(orderInfo);//保存订单信息
        //保存订单项
        for (TbOrderItem orderItem:orderInfo.getOrderItems()){
            TbOrderItem  orderItem1 = new TbOrderItem();
            orderItem1.setId(jedisClient.incr("ORDER_ITEM")+"");
            orderItem1.setItemId(orderItem.getItemId());
            orderItem1.setOrderId(orderId+"");//订单号
            orderItem1.setNum(orderItem.getNum());//购买数量
            orderItem1.setTitle(orderItem.getTitle());//商品名称
            orderItem1.setPrice(orderItem.getPrice());//商品单价
            orderItem1.setTotalFee(orderItem.getTotalFee());//商品总金额
            orderItem1.setPicPath(orderItem.getPicPath());//图片地址
            orderItemMapper.insert(orderItem1);
        }
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId+"");
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        //保存收货信息
        shippingMapper.insert(orderInfo.getOrderShipping());
        return E3Result.ok(orderId);
    }
}
