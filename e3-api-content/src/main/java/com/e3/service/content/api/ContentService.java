package com.e3.service.content.api;

import com.e3.E3Result;
import com.e3.TreeResult;
import com.e3.service.content.pojo.TbContent;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by CYJ on 2018/3/12.
 */
public interface ContentService {

    List<TreeResult> queryContentCategtory(long parentid);
    E3Result saveContent(TbContent tbContent);
    List<TbContent> queryContentList(long categoryId);

    PageInfo<TbContent> queryContent(long categoryId, Integer page, Integer rows);
}
