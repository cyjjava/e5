package com.e3.web.interceptor;

import com.e3.CookieUtils;
import com.e3.E3Result;
import com.e3.HttpClientUtil;
import com.e3.service.user.pojo.TbUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
public class MyInterceptor  implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       //1.从cookie中获取token
        //2.未获取到token,跳转登录页面

        String url = request.getRequestURI();//当前请求的url
        String  token = CookieUtils.getCookieValue(request,"TT_TOKEN",true);
        if (StringUtils.isBlank(token)){
            response.sendRedirect("http://localhost:8084/page/login?redirect=http://localhost:8086"+url);
            return  false;
        }
        //3.获取到token,查询用户信息,未查询到的话,说明用户登录已经过期
        String  jsondata =  HttpClientUtil.doGet("http://localhost:8084/user/token/"+token);
        E3Result result  = E3Result.formatToPojo(jsondata, TbUser.class);
        TbUser tbUser = (TbUser) result.getData();
        if (tbUser!=null){
            request.setAttribute("orderUser",tbUser);
            return  true;
        }
        //4.跳转登录页面
        response.sendRedirect("http://localhost:8084/page/login?redirect="+url);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
