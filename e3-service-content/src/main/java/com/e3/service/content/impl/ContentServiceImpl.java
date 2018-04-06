package com.e3.service.content.impl;

import com.e3.E3Result;
import com.e3.TreeResult;
import com.e3.jedis.dao.JedisClient;
import com.e3.service.content.api.ContentService;
import com.e3.service.content.mapper.TbContentCategoryMapper;
import com.e3.service.content.mapper.TbContentMapper;
import com.e3.service.content.pojo.TbContent;
import com.e3.service.content.pojo.TbContentCategory;
import com.e3.service.content.pojo.TbContentCategoryExample;
import com.e3.service.content.pojo.TbContentExample;
import com.e3.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CYJ on 2018/3/12.
 */
@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;
    @Autowired
    private TbContentMapper tbContentMapper;

    @Autowired
    private JedisClient jedisClient;
    @Override
    public List<TreeResult> queryContentCategtory(long parentid) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentid);
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        List<TreeResult>  treeResults = new ArrayList<>();
        for (TbContentCategory  content:list){
            TreeResult result = new TreeResult();
            result.setId(content.getId());
            result.setText(content.getName());
            result.setState(content.getIsParent()?"closed":"opent");
            treeResults.add(result);
        }
        return treeResults;
    }

    //保存内容
    @Transactional
    @Override
    public E3Result saveContent(TbContent tbContent) {
       // 删除jedis缓存
        jedisClient.hdel("INDEX_CONTENT",tbContent.getCategoryId()+"");
        tbContent.setUpdated(new Date());
        tbContent.setCreated(new Date());
        tbContentMapper.insert(tbContent);
        return E3Result.ok();
    }

    //查询内容
    public List<TbContent> queryContentList(long categoryId) {

        //从redis中取数据
        String jsonData =  jedisClient.hget("INDEX_CONTENT",categoryId+"");
        //apache下的工具类
        if(StringUtils.isNotBlank(jsonData)){
            //数据转换
            List<TbContent>  tbContents = JsonUtils.jsonToList(jsonData,TbContent.class);
            //进行返回
            return tbContents;
        }
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = tbContentMapper.selectByExample(example);
        //添加缓存的目的是为了缓解mysql数据库的压力
        //1.按照代码的执行顺序,  首先先从redis缓冲中取数据
        //2.取到以后就直接return返回
        //3.未取到,去数据库中查询,把数据库中的查询到的内容放到redis中
        //4.返回数据库中查询到的数据

        //把数据存到redis中
        jedisClient.hset("INDEX_CONTENT",categoryId+"", JsonUtils.objectToJson(list));
        return list;
    }


    @Override
    public PageInfo<TbContent> queryContent(long categoryId, Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        TbContentExample tbContentExample =new  TbContentExample();
        TbContentExample.Criteria criteria =tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list= tbContentMapper.selectByExample(tbContentExample);
        PageInfo<TbContent> eu=new PageInfo(list);
        return eu;

    }
}

