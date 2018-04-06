package com.e3.cart.service;

import com.e3.service.goods.pojo.TbItem;

import java.util.List;

/**
 * Created by CYJ on 2018/3/26.
 */
public interface CartService {

     void addCart(long userid, long itemId, int num);

    void megerCart(Long id, List<TbItem> list);

    List<TbItem> showCatList(Long id);

    void delCart(long userid,long itemId);

    void updateCart(long userid,long itemId,Integer num);

}
