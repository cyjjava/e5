package com.e3.item.controller;

import com.e3.E3Result;
import com.e3.EUDatagridResult;
import com.e3.service.goods.api.ItemParamService;
import com.e3.service.goods.pojo.TbItemParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by CYJ on 2018/3/9.
 */
@Controller
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    @RequestMapping("/item/param/list")
    @ResponseBody
    public EUDatagridResult showItemParamList(int page, int rows){
        return  itemParamService.getItemParamList(page,rows);
    }

    //根据商品类目查询规格模板
    @RequestMapping("/item/param/query/itemcatid/{cid}")
    @ResponseBody
    public E3Result queryByItemCid(@PathVariable long cid){
        TbItemParam tbItemParam = itemParamService.findByCatId(cid);
        return E3Result.ok(tbItemParam);
    }

    //保存规格模板参数
    @RequestMapping("/item/param/save/{cid}")
    @ResponseBody
    public E3Result saveParam(@PathVariable long cid,String paramData){
        TbItemParam param = new TbItemParam();
        param.setItemCatId(cid);
        param.setParamData(paramData);
        return itemParamService.saveItemParam(param);
    }

    //展示规格参数
    @RequestMapping("/item/params/{itemId}")
    public String  showParam(@PathVariable long itemId,Model model){
        String param  = itemParamService.queryByParamItem(itemId);
        model.addAttribute("paramData",param);
        return "itemParam";
    }


}
