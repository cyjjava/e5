package com.e3.interceptor;


import com.e3.ResourcesUtil;
import com.e3.service.user.pojo.ActiveUser;
import com.e3.service.user.pojo.SysPermission;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/3/6 0006.
 */
public class PermisionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取到请求的url
        String  url  = request.getRequestURI();
        //2.判断是否允许匿名访问的页面    login   error
        //2.判断是否允许匿名访问的页面    login   error
        List<String> openURL = ResourcesUtil.gekeyList("login");//读取properties文件

        for (String open_url:openURL){//遍历
            if(url.indexOf(open_url)>0){//判断
                return true;
            }
        }

        //3.从session中获取数据,如果获取到到放行
        ActiveUser activeUser = (ActiveUser) request.getSession().getAttribute("activeUser");


        List<String> openURLs = ResourcesUtil.gekeyList("commons");//读取properties文件

        for (String open_url:openURLs){//遍历
            if(url.indexOf(open_url)>0){//判断
                return true;
            }
        }
        //获取到url
        List<SysPermission>  listpermission = activeUser.getPermissions();
        for(SysPermission permission:listpermission){
            //获取url
            String  openPermissionUrl = permission.getUrl();
            if (openPermissionUrl.indexOf(url)>=0){
                return true;
            }
        }
        //4.否则,返回登录页面
        request.getRequestDispatcher("/WEB-INF/jsp/refuse.jsp").forward(request,response);


        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //进入url之前
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //进入URL之后
    }
}
