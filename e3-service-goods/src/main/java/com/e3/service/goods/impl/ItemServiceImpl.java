package com.e3.service.goods.impl;

import com.e3.E3Result;
import com.e3.EUDatagridResult;
import com.e3.IDUtils;
import com.e3.service.goods.api.ItemService;
import com.e3.service.goods.mapper.TbItemDescMapper;
import com.e3.service.goods.mapper.TbItemMapper;
import com.e3.service.goods.mapper.TbItemParamItemMapper;
import com.e3.service.goods.pojo.TbItem;
import com.e3.service.goods.pojo.TbItemDesc;
import com.e3.service.goods.pojo.TbItemDescExample;
import com.e3.service.goods.pojo.TbItemParamItem;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

/**
 * Created by CYJ on 2018/3/5.
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper iMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;
    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource(name="topicDestination")
    private Destination destination;
    @Override
    public TbItem queryByItemId(long id) {
        return iMapper.selectByPrimaryKey(id);
    }

    @Override
    public EUDatagridResult selectAllByPage(int page, int rows) {
        PageHelper.startPage(page,rows);
        Page<TbItem> result = (Page<TbItem>) iMapper.selectByExample(null);
        //总条数     返回的结果集合
        return new EUDatagridResult(result.getTotal(),result.getResult()) ;
    }



    //保存商品
   @Transactional
    @Override
    public E3Result saveItem(TbItem tbItem, String desc, String itemParams) throws Exception {
        //商品id  使用工具类生成
        long itemId = IDUtils.genItemId();
        //数据补全
        tbItem.setStatus((byte)1);//商品状态，1-正常，2-下架，3-删除',
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        tbItem.setId(itemId);
        iMapper.insert(tbItem);//保存商品
        //保证两条sql语句同事执行成功
        E3Result result = this.saveItemDesc(itemId,desc);
        if(result.getStatus()!=200){
            throw  new Exception();//交给spring处理异常
        }
       E3Result resultParam=this.saveItemParam(itemId,itemParams);
       if(resultParam.getStatus()!=200){
           throw  new Exception();//交给spring处理异常
       }

       //发送activeMQ消息  topic
         //发送一个商品添加消息
       jmsTemplate.send(destination, new MessageCreator() {
           @Override
           public Message createMessage(Session session) throws JMSException {
               TextMessage textMessage = session.createTextMessage(itemId + "");
               return textMessage;
           }
       });
        return E3Result.ok();
    }

    @Override
    public TbItemDesc queryByItemIdDesc(long itemid) {
        TbItemDescExample example = new TbItemDescExample();
        TbItemDescExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemid);
        List<TbItemDesc> list = tbItemDescMapper.selectByExampleWithBLOBs(example);
        if (list!=null &&list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Transactional
//保存详情
    private  E3Result saveItemDesc(long itmId, String desc){
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setUpdated(new Date());
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setItemId(itmId);
        tbItemDesc.setItemDesc(desc);
        tbItemDescMapper.insert(tbItemDesc);
        return E3Result.ok();
    }

    @Transactional
//保存规格参数信息
    private  E3Result  saveItemParam(long itemId,String itemParams){
        TbItemParamItem itemParamItem  = new TbItemParamItem();
        itemParamItem.setUpdated(new Date());
        itemParamItem.setCreated(new Date());
        itemParamItem.setItemId(itemId);
        itemParamItem.setParamData(itemParams);
        tbItemParamItemMapper.insert(itemParamItem);
        return  E3Result.ok();
    }

}
