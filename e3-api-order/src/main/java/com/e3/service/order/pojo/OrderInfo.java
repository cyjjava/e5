package com.e3.service.order.pojo;

import java.util.List;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
public class OrderInfo  extends  TbOrder implements Serializable{

    private List<TbOrderItem>  orderItems;

    private TbOrderShipping orderShipping;

    public OrderInfo() {
    }

    public OrderInfo(List<TbOrderItem> orderItems, TbOrderShipping orderShipping) {
        this.orderItems = orderItems;
        this.orderShipping = orderShipping;
    }

    public List<TbOrderItem> getOrderItems() {

        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
