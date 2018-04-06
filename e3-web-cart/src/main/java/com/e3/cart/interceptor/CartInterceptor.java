package com.e3.cart.interceptor;

import com.e3.CookieUtils;

import com.e3.jedis.dao.JedisClient;
import com.e3.service.user.pojo.TbUser;
import com.e3.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018/3/22 0022.
 */
public class CartInterceptor implements HandlerInterceptor {
    @Autowired
    private JedisClient jedisClient;

    //panduan duan
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.通过token    在cookie里的可以   TT_TOKEN
        String   token =   CookieUtils.getCookieValue(request,"TT_TOKEN");
        if(StringUtils.isBlank(token)){//没有取到token
            return true;
        }
        //2.通过token码去redis中查询
        String jsondata = jedisClient.get("REDIS_USER:" + token);
        //判断是否取到
        if(StringUtils.isNotBlank(jsondata)){
            TbUser    tbUser  = JsonUtils.jsonToPojo(jsondata,TbUser.class);
            request.setAttribute("tbUser",tbUser);
            return true;
        }
        //3.查询成功,把用户数据放到request作用域中
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
