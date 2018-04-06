package com.e3.service.search.mapper;

import com.e3.service.search.pojo.SearchDTO;

import java.util.List;

/**
 * Created by CYJ on 2018/3/15.
 */

public interface SearchMapper {

    List<SearchDTO> queryAllIdnex();

    SearchDTO  queryByItemId(long itemId);

}
