package com.shsxt.crm;

import com.alibaba.fastjson.JSON;
import com.shsxt.crm.exceptions.NoLoginException;
import com.shsxt.crm.exceptions.ParamsException;
import com.shsxt.crm.model.ResultInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mv = new ModelAndView();
        if(ex instanceof NoLoginException){
            NoLoginException ne = (NoLoginException) ex;
            mv.setViewName("no_login");
            mv.addObject("msg",ne.getMsg());
            mv.addObject("ctx",request.getContextPath());
            return mv;
        }



        mv.setViewName("errors");
        mv.addObject("code",400);
        mv.addObject("msg","系统异常，请稍后再试。。。");
//判断消息处理的信息是否是json
        if(handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
            ResponseBody responseBody = hm.getMethod().getDeclaredAnnotation(ResponseBody.class);
            if (null==responseBody){
                //方法返回视图

                if(ex instanceof ParamsException){
                    ParamsException pe = (ParamsException) ex;
                    mv.addObject("msg",pe.getMsg());
                    mv.addObject("code",pe.getCode());
                }
                return mv;
            }else {
                //方法返回json
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统错误，请稍后再试");
                if (ex instanceof ParamsException){
                    ParamsException pe = (ParamsException) ex;
                    resultInfo.setCode(pe.getCode());
                    resultInfo.setMsg(pe.getMsg());

                }

                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                PrintWriter pw = null;
                try {
                    pw=response.getWriter();
                    pw.write(JSON.toJSONString(resultInfo));
                    pw.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(null!=pw){
                        pw.close();
                    }
                }
                return null;
            }
        }else {
            return mv;
        }

    }
}
