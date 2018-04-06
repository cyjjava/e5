package com.e3.content.controller;

import com.e3.E3Result;
import com.e3.EUDatagridResult;
import com.e3.TreeResult;
import com.e3.service.content.api.ContentService;
import com.e3.service.content.pojo.TbContent;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Administrator on 2018/3/12 0012.
 */
@RestController
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping("/content/category/list")
    public List<TreeResult> queryCategotry(@RequestParam(value = "id",defaultValue = "0") long parentid) {
        List<TreeResult> list = contentService.queryContentCategtory(parentid);
        return list;

    }

    //保存内容
    @RequestMapping("/content/save")
    public E3Result saveContent(TbContent tbContent){
        return contentService.saveContent(tbContent);
    }

    //全查分类展示
    @RequestMapping("/content/query/list")
    @ResponseBody
    public EUDatagridResult queryContent(@RequestParam(value = "categoryId",defaultValue = "0") long categoryId, Integer page, Integer rows) {
        PageInfo<TbContent> pageInfo = contentService.queryContent(categoryId,page,rows);
        EUDatagridResult eu=new EUDatagridResult(pageInfo.getTotal(),pageInfo.getList());
        return eu;
    }
}
