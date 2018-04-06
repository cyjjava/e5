package com.e3.service.goods.api;

import com.e3.service.goods.pojo.TbItemCat;

import java.util.List;

/**
 * Created by CYJ on 2018/3/7.
 */
public interface ItemCatService {

    List<TbItemCat>  queryTreeList(long parentId);
}
