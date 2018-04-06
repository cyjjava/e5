package com.e3.service.goods.impl;

import com.e3.E3Result;
import com.e3.EUDatagridResult;
import com.e3.service.goods.api.ItemParamService;
import com.e3.service.goods.mapper.TbItemParamItemMapper;
import com.e3.service.goods.mapper.TbItemParamMapper;
import com.e3.service.goods.pojo.TbItemParam;
import com.e3.service.goods.pojo.TbItemParamExample;
import com.e3.service.goods.pojo.TbItemParamItem;
import com.e3.service.goods.pojo.TbItemParamItemExample;
import com.e3.utils.JsonUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by CYJ on 2018/3/9.
 */
@Service
public class ItemParamServiceImpl implements ItemParamService {

    @Autowired
    private TbItemParamMapper tbItemParamMapper;
    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;


    @Override
    public EUDatagridResult getItemParamList(int page, int rows) {

        PageHelper.startPage(page,rows);
        Page<TbItemParam>  result = (Page<TbItemParam>) tbItemParamMapper.querySelectParamList();
        return new EUDatagridResult(result.getTotal(),result.getResult());//fenye
    }


    //根据商品类目id查询规格模板
    @Override
    public TbItemParam findByCatId(long cid) {
        TbItemParamExample example = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = example.createCriteria();
        criteria.andItemCatIdEqualTo(cid);
        List<TbItemParam> tbItemParams = tbItemParamMapper.selectByExampleWithBLOBs(example);
        if(tbItemParams!=null && tbItemParams.size()>0){
            return tbItemParams.get(0);
        }

        return null;
    }

    //保存规格模板
    @Override
    public E3Result saveItemParam(TbItemParam tbItemParam) {
        //数据补全
        tbItemParam.setCreated(new Date());
        tbItemParam.setUpdated(new Date());
        tbItemParamMapper.insert(tbItemParam);
        return E3Result.ok();
    }

    //查询商品规格参数
    @Override
    public String queryByParamItem(long itemId) {
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem>  tbItemParamItems = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
        //查询出
        String paramData = null;

        if(tbItemParamItems!=null && tbItemParamItems.size()>0){
            paramData = tbItemParamItems.get(0).getParamData();//获取paramdata字段中内容
        }

        //解析paramData这个json字符串

        List<Map>  listMap = JsonUtils.jsonToList(paramData,Map.class);//
        StringBuffer   sb= new StringBuffer();
        for (Map m1:listMap){
            sb.append("<div class=\"Ptable-item\">\n");
            sb.append("        <h3>"+m1.get("group")+"</h3>\n");
            List<Map>  m2 = (List<Map>) m1.get("params");
            for (Map  m3:m2) {
                sb.append("        <dl>\n");
                sb.append("       <dt>"+m3.get("k")+"</dt>\n");
                sb.append("       <dd>"+m3.get("v")+"</dd>\n");
                sb.append("    </dl>\n");
            }
            sb.append("</div>");
        }
        return sb.toString();
    }
}




