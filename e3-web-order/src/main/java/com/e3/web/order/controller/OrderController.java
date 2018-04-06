package com.e3.web.order.controller;


import com.e3.CookieUtils;
import com.e3.E3Result;
import com.e3.order.service.OrderServices;
import com.e3.service.goods.pojo.TbItem;
import com.e3.service.order.api.OrderService;
import com.e3.service.order.pojo.OrderInfo;
import com.e3.service.order.pojo.TbOrder;
import com.e3.service.user.pojo.TbUser;
import com.e3.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
@Controller
public class OrderController {

    @Autowired
    private OrderServices orderService;


    @Autowired
    private OrderService orderServiceitem;

    @RequestMapping("/order/order-cart")
    public  String  showcart(Model model, HttpServletRequest request, HttpServletResponse response){
        List<TbItem> lists = getCartList(request);//从cookie中取的信息
        TbUser tbUser = (TbUser) request.getAttribute("orderUser");//取作用域中的user信息
        if (tbUser!=null){//判断是否为空
            orderService.megareCart(tbUser.getId(),lists);//合并购物车
            //展示的是redis中购物车的信息
            lists = orderService.showCart(tbUser.getId());
            //清空cookie;
            CookieUtils.deleteCookie(request,response,"TT_CART");
        }
        model.addAttribute("cartList",lists);
        return "order-cart";
    }

    //从cookie中取数据
    private java.util.List<TbItem> getCartList(HttpServletRequest request){
        String jsonData =   CookieUtils.getCookieValue(request,"TT_CART",true);
        if(StringUtils.isNotBlank(jsonData)){
            return (java.util.List<TbItem>) JsonUtils.jsonToList(jsonData,TbItem.class);
        }
        return  new ArrayList<TbItem>();
    }

    //提交订单
    @RequestMapping("/order/create")
    public String  createOrder(HttpServletRequest request, Model model,OrderInfo orderInfo){
        //获取用户信息
        TbUser tbUser = (TbUser) request.getAttribute("orderUser");
        TbOrder tbOrder = new TbOrder();
        tbOrder.setUserId(tbUser.getId());
        tbOrder.setBuyerNick(tbUser.getUsername());
        E3Result order = orderServiceitem.createOrder(orderInfo);
        long orderid = (long) order.getData();
        model.addAttribute("orderId",orderid);
       String payment = orderInfo.getPayment();//总价格
        model.addAttribute("payment",payment);
        return "success";
    }
}
