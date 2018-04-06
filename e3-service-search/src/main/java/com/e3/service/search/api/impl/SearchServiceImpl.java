package com.e3.service.search.api.impl;

import com.e3.E3Result;
import com.e3.service.search.api.SearchService;
import com.e3.service.search.dao.SearchDAO;
import com.e3.service.search.mapper.SearchMapper;
import com.e3.service.search.pojo.SearchDTO;
import com.e3.service.search.pojo.SearchPojo;
import com.e3.service.search.pojo.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CYJ on 2018/3/15.
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrServer solrServer;
    @Autowired
    private SearchMapper searchMapper;
    @Autowired
    private SearchDAO searchDAO;

    @Override
    public E3Result importIndexItem() {
        //1.从数据库中查询出数据
        //2.把数据写入到索引库中
        try {
            List<SearchDTO> list = searchMapper.queryAllIdnex();
            for (SearchDTO  searchDTO : list){
                SolrInputDocument solrInputDocument = new SolrInputDocument();
                solrInputDocument.addField("id",searchDTO.getId());
                solrInputDocument.addField("item_title",searchDTO.getTitle());
                solrInputDocument.addField("item_image",searchDTO.getImage());
                solrInputDocument.addField("item_desc",searchDTO.getItemDesc());
                solrInputDocument.addField("item_category_name",searchDTO.getItemName());
                solrInputDocument.addField("item_price",searchDTO.getPrice());
                solrInputDocument.addField("item_sell_point",searchDTO.getSellPoint());
                solrServer.add(solrInputDocument);
            }
            solrServer.commit();//提交
        }catch(Exception e){
            e.printStackTrace();
        }
        return E3Result.ok();
    }

    @Override
    public SearchResult search(String query, Integer pageNow, Integer pageSize) {
        //1.创建对象solrquery
        //2.添加条件
        //3.调用dao
        //4.封装结果
        //5.返回结果
        //1.设置查询条件
        SolrQuery solrQuery = new SolrQuery();
        //设置默认搜索域
        solrQuery.set("df","item_keywords");
        //设置查询条件
        if(StringUtils.isNotBlank(query)){
            solrQuery.setQuery(query);
        }else{
            solrQuery.setQuery("*:*");
        }
        //2.设置高亮显示字段
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<font color=red>");
        solrQuery.setHighlightSimplePost("</font>");
        //3.计算总页数

        if(pageNow<0)  pageNow=1;
        if (pageNow==0) pageNow=1;
        //4.计算当前页
        int pageNows= (pageNow-1)*pageSize;
        solrQuery.setStart(pageNows);
        solrQuery.setRows(pageSize);
        List<SearchPojo> list = searchDAO.querySearch(solrQuery);
        SearchResult result = new SearchResult();
        result.setItemList(list);
        result.setCurPage(pageNow);
        result.setRecordCount(list.size());
        //总页数
        int totalPage= (list.size()+pageSize-1)/pageSize;
        result.setPageCount(totalPage);
        return result;
    }

    @Override
    public E3Result sysnIndexSearch(long itemId) {
        SearchDTO  searchDTO = searchMapper.queryByItemId(itemId);
        try {
            SolrInputDocument solrInputDocument = new SolrInputDocument();
            solrInputDocument.addField("id", searchDTO.getId());
            solrInputDocument.addField("item_title", searchDTO.getTitle());
            solrInputDocument.addField("item_image", searchDTO.getImage());
            solrInputDocument.addField("item_desc", searchDTO.getItemDesc());
            solrInputDocument.addField("item_category_name", searchDTO.getItemName());
            solrInputDocument.addField("item_price", searchDTO.getPrice());
            solrInputDocument.addField("item_sell_point", searchDTO.getSellPoint());
            solrServer.add(solrInputDocument);

            solrServer.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
        return E3Result.ok();
    }
}

