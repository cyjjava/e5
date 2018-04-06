package com.e3.item.controller;

import com.e3.service.goods.api.ItemCatService;
import com.e3.service.goods.pojo.TbItemCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CYJ on 2018/3/7.
 */
@RestController
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("/item/cat/list")
    public List<Map> queryListTree(@RequestParam(value = "id",defaultValue = "0") long id){
        List<TbItemCat> list = itemCatService.queryTreeList(id);
        List<Map> resultList = new ArrayList<>();
        for(TbItemCat tbItemCat: list){
            Map map = new HashMap();
            map.put("id",tbItemCat.getId());
            map.put("text",tbItemCat.getName());
            map.put("state",tbItemCat.getIsParent()?"closed":"open");
            resultList.add(map);
        }
        return resultList;
    }

}
