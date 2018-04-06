package com.e3.search.controller;

import com.e3.E3Result;
import com.e3.service.search.api.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by CYJ on 2018/3/15.
 */
@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;
    @RequestMapping("/index/item/import")
    @ResponseBody
    public E3Result importIndex(){
        return  searchService.importIndexItem();
    }
}
