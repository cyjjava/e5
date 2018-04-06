package com.e3.service.user.impl;

import com.e3.E3Result;
import com.e3.jedis.dao.JedisClient;
import com.e3.service.user.api.UserService;
import com.e3.service.user.mapper.SysPermissionMenuMapper;
import com.e3.service.user.mapper.SysUserMapper;
import com.e3.service.user.mapper.TbUserMapper;
import com.e3.service.user.pojo.*;
import com.e3.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by CYJ on 2018/3/6.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private SysPermissionMenuMapper sysPermissionMenuMapper;

    @Override
    public E3Result authRityUsername(String usercode, String pwd) {
        //1.根据用户名查询用户信息
        SysUser  sysUser = this.getByUserCode(usercode);
        if(sysUser==null){
            //2.没有查询到信息,改用户不存
            return  E3Result.build(400,"该用户不存在!");
        }
        //3.如果查询到,判断用户名和密码是否匹配
        //3.1从数据库里获取密码
        String pwd_db = sysUser.getPassword();
        //3.2获取到前台输入的密
        String salt = sysUser.getSalt();//获取该用户的盐值
        //3.3 把前台获取到的密码+盐值进行md5加密
        String  pwd_input = DigestUtils.md5DigestAsHex((pwd+salt).getBytes());
        //3.4 数据库的密码与前台的密码进行匹配
         List<SysPermission>  menuList=sysPermissionMenuMapper.findByMenu(sysUser.getId());
        List<SysPermission> permissionList = sysPermissionMenuMapper.findByPermission(sysUser.getId());

        if(pwd_db.equals(pwd_input)){

            ActiveUser activeUser = new ActiveUser();
            activeUser.setUserid(sysUser.getId());
            activeUser.setUsername(sysUser.getUsername());
            activeUser.setUsercode(sysUser.getUsercode());
            activeUser.setMenus(menuList);
            activeUser.setPermissions(permissionList);
            return E3Result.ok(activeUser);//返回的是200那个信息
        }else {
            //4.如果匹配,返回200
            return  E3Result.build(400,"用户名或者密码错误!");
        }
    }
   //验证用户的数据
    @Override
    public E3Result checkUserParam(String params, int type) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //1、2、3分别代表username、phone、email
        if(type==1){
            criteria.andUsernameEqualTo(params);
        }
        if(type==2){
            criteria.andPhoneEqualTo(params);
        }
        if(type==3){
            criteria.andEmailEqualTo(params);
        }
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if(list!=null && list.size()>0){//判断,如果查询出数据,不可用
            return E3Result.ok(false);
        }
        return E3Result.ok(true);
    }

    //用户注册
    @Override
    public E3Result registerUser(TbUser tbUser) {
        //数据补全
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        tbUser.setPassword(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));//密码加密

        //数据校验,username,email,phone必须是唯一的
        E3Result result = this.checkUserParam(tbUser.getUsername(),1);
        if(!(Boolean) result.getData()){
            return E3Result.build(400,"用户已使用!");
        }
        E3Result result1 = this.checkUserParam(tbUser.getPhone(),2);
        if(!(Boolean) result1.getData()){
            return E3Result.build(400,"该电话已注册过!");
        }
        E3Result result2 = this.checkUserParam(tbUser.getEmail(),3);
        if(!(Boolean) result2.getData()){
            return E3Result.build(400,"该邮箱已经注册!");
        }

        tbUserMapper.insert(tbUser);

        return E3Result.ok();
    }

    //登录
    @Override
    public E3Result loginUser(String username, String password) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if (list ==null ){
            return  E3Result.build(400,"该用户不存在");
        }
        TbUser  tbUser = list.get(0);
        String pwd = tbUser.getPassword();
        String pwd_input = DigestUtils.md5DigestAsHex(password.getBytes());

        //判断密码
        if(pwd.equals(pwd_input)){
            String token = UUID.randomUUID().toString();
            tbUser.setPassword(null);
            //把用户信息存到redis中
            jedisClient.set("REDIS_USER:"+token, JsonUtils.objectToJson(tbUser));
            //设置生命周期
            jedisClient.expire("REDIS_USER:"+token,7200);

            return E3Result.ok(token);
        }
        return E3Result.build(400,"用户名或者密码错误!");
    }

    //通过token查询用户信息
    @Override
    public E3Result getBytoken(String token) {
        String  jsonData =  jedisClient.get("REDIS_USER:"+token);
        if(StringUtils.isNotBlank(jsonData)){
            TbUser tbUser = JsonUtils.jsonToPojo(jsonData,TbUser.class);
            return E3Result.ok(tbUser);
        }
        return E3Result.build(400,"该用户已经过期!");
    }

     //安全退出
    @Override
    public E3Result delToken(String token) {
        jedisClient.del("REDIS_USER:"+token);
        return E3Result.ok();
    }

    //根据token码换取用户信息
    @Override
    public E3Result getBytokenUser(String token) {
        String jsondata = jedisClient.get("USER_SESSION_REDIS:"+token);
        if (StringUtils.isNotBlank(jsondata)){
            TbUser  tbUser = JsonUtils.jsonToPojo(jsondata,TbUser.class);
            return E3Result.ok(tbUser);
        }
        return E3Result.ok();
    }

    //查询用户信息
    private SysUser getByUserCode(String usercode){
        SysUserExample example = new SysUserExample();
        //自动补全返回类型  ctrl+alt+v 快捷方式
        SysUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsercodeEqualTo(usercode);
        List<SysUser> sysUsers = userMapper.selectByExample(example);
        if(sysUsers!=null){
            return sysUsers.get(0);
        }
        return null;
    }
}

