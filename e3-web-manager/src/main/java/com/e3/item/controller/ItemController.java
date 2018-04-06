package com.e3.item.controller;

import com.e3.E3Result;
import com.e3.EUDatagridResult;
import com.e3.service.goods.api.ItemService;
import com.e3.service.goods.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by CYJ on 2018/3/5.
 */
@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public TbItem getItemById(@PathVariable Long itemId) {
        TbItem tbItem = itemService.queryByItemId(itemId);
        return tbItem;
    }

    @RequestMapping("/item/list")
    public EUDatagridResult selectAllByPage(int page, int rows) {

        EUDatagridResult result = itemService.selectAllByPage(page, rows);
       return result;

    }

    @RequestMapping("/item/save")
    public E3Result saveTbItem(TbItem tbItem, String desc,String itemParams)throws  Exception{
        E3Result result = itemService.saveItem(tbItem,desc,itemParams);
        return result;
    }

}
