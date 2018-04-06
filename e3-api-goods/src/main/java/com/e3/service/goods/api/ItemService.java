package com.e3.service.goods.api;

import com.e3.E3Result;
import com.e3.EUDatagridResult;
import com.e3.service.goods.pojo.TbItem;
import com.e3.service.goods.pojo.TbItemDesc;

/**
 * Created by CYJ on 2018/3/5.
 */
public interface ItemService {

    TbItem queryByItemId(long id);

    EUDatagridResult selectAllByPage(int page, int rows);

    //保存商品
    E3Result saveItem(TbItem tbItem,String desc,String itemParams) throws Exception;

    TbItemDesc queryByItemIdDesc(long itemid);

}
