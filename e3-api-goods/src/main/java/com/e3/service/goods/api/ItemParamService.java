package com.e3.service.goods.api;

import com.e3.E3Result;
import com.e3.EUDatagridResult;
import com.e3.service.goods.pojo.TbItemParam;

/**
 * Created by CYJ on 2018/3/9.
 */
public interface ItemParamService {

    EUDatagridResult getItemParamList(int page,int rows);

    TbItemParam findByCatId(long cid);

    E3Result saveItemParam(TbItemParam tbItemParam);

    String queryByParamItem(long itemId);
}
