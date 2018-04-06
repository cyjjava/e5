package com.e3.service.goods.impl;

import com.e3.service.goods.api.ItemCatService;
import com.e3.service.goods.mapper.TbItemCatMapper;
import com.e3.service.goods.pojo.TbItemCat;
import com.e3.service.goods.pojo.TbItemCatExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CYJ on 2018/3/7.
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper catMapper;

    @Override
    public List<TbItemCat> queryTreeList(long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat>  list = catMapper.selectByExample(example);
        return list;
    }
}
