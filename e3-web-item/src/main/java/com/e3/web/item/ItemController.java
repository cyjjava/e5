package com.e3.web.item;

import com.e3.service.goods.api.ItemService;
import com.e3.service.goods.pojo.TbItem;
import com.e3.service.goods.pojo.TbItemDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by CYJ on 2018/3/18.
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String queryByItemid(@PathVariable long itemId, Model model){
        TbItem tbItem = itemService.queryByItemId(itemId);
        TbItemDesc tbItemDesc = itemService.queryByItemIdDesc(itemId);
        model.addAttribute("item",tbItem);
        model.addAttribute("itemDesc",tbItemDesc);
        return "item";
    }
}
