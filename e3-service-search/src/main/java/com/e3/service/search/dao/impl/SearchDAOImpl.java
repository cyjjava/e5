package com.e3.service.search.dao.impl;

import com.e3.service.search.dao.SearchDAO;
import com.e3.service.search.pojo.SearchPojo;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by CYJ on 2018/3/15.
 */
@Repository
public class SearchDAOImpl implements SearchDAO {

    @Autowired
    private SolrServer solrServer;

    @Override
    public List<SearchPojo> querySearch(SolrQuery solrQuery) {
        //执行查询条件
        //2,返回查询结果,把结果封装到searchpojo的集合中
        //3.结果中取一下高亮显示内容比较复杂
        //1.执行查询
        //2.获取查询结果
        //3.返回查询结果
        List<SearchPojo>  resultlist = new ArrayList<>();
        try {
            QueryResponse response = solrServer.query(solrQuery);
            SolrDocumentList list = response.getResults();
            for (SolrDocument solrDocument:list ) {
                SearchPojo pojo = new SearchPojo();
                pojo.setId((String) solrDocument.get("id"));
                //取高亮显示
                Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
                if (highlighting!=null){
                    List<String> strings = highlighting.get(solrDocument.get("id")).get("item_title");
                    if (strings!=null && strings.size()>0){
                        pojo.setTitle(strings.get(0));
                    }else{
                        pojo.setTitle((String) solrDocument.get("item_title"));
                    }
                }else{
                    pojo.setTitle((String) solrDocument.get("item_title"));
                }
                pojo.setImage((String) solrDocument.get("item_image"));
                pojo.setCatName((String) solrDocument.get("item_category_name"));
                pojo.setItem_desc((String) solrDocument.get("item_desc"));
                pojo.setPrice((Long) solrDocument.get("item_price"));
                pojo.setSell_point((String) solrDocument.get("item_sell_point"));
                resultlist.add(pojo);
            }
            return  resultlist;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    }

