package com.shsxt.crm.controller;

import com.github.pagehelper.PageException;
import com.shsxt.base.BaseController;
import com.shsxt.crm.exceptions.ParamsException;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;

    //登录页
    @RequestMapping("index")
    public String index(){
//        if(true){
//            throw new ParamsException("参数异常...");
//        }
        return "index";
    }
    //主页
    @RequestMapping("main")


    /**
     * 后台管理主页面
     */
    public String main(HttpServletRequest request){
        //使用解码工具将ID前台id进行解码
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //设置返回的参数
        request.setAttribute("user",userService.selectByPrimaryKey(userId));
        return "main";
    }
}
