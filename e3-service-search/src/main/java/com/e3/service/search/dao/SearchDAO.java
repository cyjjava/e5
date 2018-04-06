package com.e3.service.search.dao;

import com.e3.service.search.pojo.SearchPojo;
import org.apache.solr.client.solrj.SolrQuery;

import java.util.List;

/**
 * Created by CYJ on 2018/3/15.
 */
public interface SearchDAO {

    List<SearchPojo> querySearch(SolrQuery solrQuery);
}
