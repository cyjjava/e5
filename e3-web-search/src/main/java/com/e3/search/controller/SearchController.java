package com.e3.search.controller;

/**
 * Created by CYJ on 2018/3/15.
 */

import com.e3.service.search.api.SearchService;
import com.e3.service.search.pojo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping("/search")
    public String search(String keyword, @RequestParam(defaultValue = "1",value = "page")Integer page, Model model){
        SearchResult result = searchService.search(keyword, page, 60);
        model.addAttribute("itemList",result.getItemList());
        model.addAttribute("totalPages",result.getPageCount());
        model.addAttribute("page",result.getCurPage());
        model.addAttribute("recourdCount",result.getRecordCount());
        return "search";
    }
}
