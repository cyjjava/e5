package com.e3.web.sso.controller;

import com.e3.CookieUtils;
import com.e3.E3Result;
import com.e3.service.user.api.UserService;
import com.e3.service.user.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by CYJ on 2018/3/20.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;


   //用户信息验证
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkUserParam(@PathVariable String param, @PathVariable int type){
        return userService.checkUserParam(param,type);
    }

    //用户注册
    @RequestMapping("/user/register")
    @ResponseBody
    public E3Result registerUser(TbUser tbUser){
        return  userService.registerUser(tbUser);
    }


    //用户登录
    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    public E3Result loginUser(String username, String password, HttpServletRequest request, HttpServletResponse response){
        //执行
        E3Result  result = userService.loginUser(username,password);
        if (result.getStatus()==200) {
            //取出返回的token吗,把这个token存到cookie中
            CookieUtils.setCookie(request, response, "TT_TOKEN", result.getData().toString());
        }
        return  result;
    }

  /*  //通过token查询用户信息
    @RequestMapping("/user/token/{token}")
    @ResponseBody
    public  E3Result  getByToken(@PathVariable String token){
        return  userService.getBytoken(token);
    }*/

   //跨域请求解决
    @RequestMapping("/user/token/{token}")
    @ResponseBody
    public  Object  getByToken(@PathVariable String token,String callback){
        E3Result  result  =userService.getBytoken(token);
        MappingJacksonValue jacksonValue =  new MappingJacksonValue(result);
        jacksonValue.setJsonpFunction(callback);
        return  jacksonValue;
    }

    //通过token删除用户信息
    @RequestMapping("/user/logout/{token}")
    @ResponseBody
    public  E3Result  delToken(@PathVariable String token){
        return  userService.delToken(token);
    }

    //返回页面的方法
    @RequestMapping("/page/{page}")
    public String showPage(@PathVariable String page, String redirect, Model model){
        model.addAttribute("redirect",redirect);
        return page;
    }

}
