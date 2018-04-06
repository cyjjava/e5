package com.e3.service.search.api;

import com.e3.E3Result;
import com.e3.service.search.pojo.SearchResult;

/**
 * Created by CYJ on 2018/3/15.
 */
public interface SearchService {

    E3Result importIndexItem();

    SearchResult search(String query, Integer pageNow, Integer pageSize);

    E3Result sysnIndexSearch(long itemId);
}
