package com.e3.order.service;

import com.e3.service.goods.pojo.TbItem;
import java.util.List;


/**
 * Created by Administrator on 2018/1/10 0010.
 */
public interface OrderServices {
    void  megareCart(long userid, List<TbItem> list);

    List<TbItem> showCart(long userid);

    void addCart(long userid, long itemid, Integer num) ;
}
