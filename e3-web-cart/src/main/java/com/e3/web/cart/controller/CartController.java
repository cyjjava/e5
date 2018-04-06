package com.e3.web.cart.controller;

import com.e3.CookieUtils;
import com.e3.E3Result;
import com.e3.cart.service.CartService;
import com.e3.service.goods.api.ItemService;
import com.e3.service.goods.pojo.TbItem;
import com.e3.service.user.pojo.TbUser;
import com.e3.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/21
 */
@Controller
public class CartController {

    @Autowired
    private ItemService  itemService;
    @Autowired
    private CartService cartService;

    //添加购物车
    @RequestMapping("/cart/add/{itemId}")
    public String addCart(HttpServletRequest  request, HttpServletResponse response,@PathVariable long itemId, Integer num){
        //1.知道购物车里是否有该商品
        List<TbItem>  cartList = getCartList(request);

        TbUser tbUser = (TbUser) request.getAttribute("tbUser");
        if(tbUser!=null){
            cartService.addCart(tbUser.getId(),itemId,num);
            return  "cartSuccess";
        }

        boolean  flag  =false;
        for(TbItem  cart :cartList){
            //2.如果有,只需要修改数量
            if(cart.getId()==itemId){
                cart.setNum(cart.getNum()+num);
                flag = true;
                break;
            }
        }
        //3.没有,就进使用商品的id,查询数据库
        if(!flag){
            TbItem tbItem = itemService.queryByItemId(itemId);
           /* String [] imags =tbItem.getImages();
            if(imags!=null && imags.length>0){
                tbItem.setImage(imags[0]);
            }*/
            cartList.add(tbItem);
        }

        //4.把数据存到cookie中
        CookieUtils.setCookie(request,response,"TT_CART", JsonUtils.objectToJson(cartList),72000,true);
        //5.给cookie设置有效时间,7天
        return "cartSuccess";
    }

    //展示
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest  request, Model model){
        List<TbItem>  list = getCartList(request);
        //判断用户是否登录
        TbUser  tbUser = (TbUser) request.getAttribute("tbUser");
        if(tbUser!=null){
            //合并   userid  查询出的数据进行合并
            cartService.megerCart(tbUser.getId(),list);
            list = cartService.showCatList(tbUser.getId());
        }
        //如果用户登录,把cookie中的信息与redis里的数据进行合并
        //展示的是redis中的数据

        model.addAttribute("cartList",list);
        return "cart";

    }
    //删除购物车商品
    @RequestMapping("/cart/delete/{itemId}")
    public String  delCart(HttpServletRequest  request,HttpServletResponse response,@PathVariable long itemId){
        List<TbItem>  list = getCartList(request);

        for(TbItem tbItem : list){
            if(tbItem.getId() == itemId){
                list.remove(tbItem);
                break;
            }
        }
        //4.把数据存到cookie中
        CookieUtils.setCookie(request,response,"TT_CART",JsonUtils.objectToJson(list),72000,true);
        //5.给cookie设置有效时间,7天

        TbUser  tbUser = (TbUser) request.getAttribute("tbUser");
        if(tbUser!=null){
            //删除redis里的购物车数据
            cartService.delCart(tbUser.getId(),itemId);
        }
        return "redirect:/cart/cart.html";
    }

    //从购物车中获取数据
    private List<TbItem>  getCartList(HttpServletRequest request){
        List<TbItem> list = null;
        //从cookie中取数据
        String  jsondata =  CookieUtils.getCookieValue(request,"TT_CART",true);
        if(StringUtils.isNotBlank(jsondata)) {
            //把字符串进行转换
            list = JsonUtils.jsonToList(jsondata, TbItem.class);
            return  list;
            //取不到数据
        }
        return new ArrayList<TbItem>();
    }

    //修改商品数量  返回E3Result
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateItemNum(HttpServletRequest request, HttpServletResponse response, @PathVariable Long  itemId, @PathVariable Integer num){
        //1从cookie中读取购物车里的商品信息
        List<TbItem>  list = getCartList(request);
        //2.遍历购物车的信息
        for (TbItem items : list){
            //3.判断如果购物车里的id与接收到的参数商品id相同
            if(items.getId()==itemId.longValue()){
                //4.重新设置商品的数量
                items.setNum(num);
                break;
            }
        }
        //5.写入到cookie中
        CookieUtils.setCookie(request,response,"TT_CART",JsonUtils.objectToJson(list),true);//把商品放入到购物车
        TbUser  tbUser = (TbUser) request.getAttribute("tbUser");
        if(tbUser!=null){
            cartService.updateCart(tbUser.getId(),itemId,num);
        }

        return E3Result.ok();
    }


}

