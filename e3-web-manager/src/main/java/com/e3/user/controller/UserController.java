package com.e3.user.controller;

import com.e3.E3Result;
import com.e3.service.user.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by CYJ on 2018/3/6.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;
    //登录
    @RequestMapping("user/login")
    public String  loginUser(String usercode, String password, Model model, HttpSession session, String randomcode){
        //判断验证码是否正确
        String   valiCode = (String) session.getAttribute("validateCode");
        if (!valiCode.equalsIgnoreCase(randomcode)){
            model.addAttribute("msg","验证码错误");
            return "error";
        }
        E3Result result = userService.authRityUsername(usercode,password);
        if(result.getStatus()==400){
            model.addAttribute("msg",result.getMsg());
            return "error";
        }
        session.setAttribute("activeUser",result.getData());
        return "redirect:/index";
    }
}
