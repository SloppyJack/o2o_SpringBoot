package com.example.o2o.interceptor.shopAdmin;

import com.example.o2o.entity.PersonInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: 店铺管理系统登录验证拦截器
 * @date: 2020/3/10 22:24
 **/
public class ShopLoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * 事前拦截，即用户操作发生前，改写preHandler里的逻辑，进行拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从session中取出用户信息来
        Object userObj = request.getSession().getAttribute("user");
        if (userObj != null){
            //若用户信息不为空，则进行转换
            PersonInfo user = (PersonInfo)userObj;
            //做空值判断，确保userId
            if (user != null && user.getUserId() != null && user.getUserStatus() == 1 )
                return true;
        }

        //若不满足登录验证，直接跳到账号登录页面
        PrintWriter out = response.getWriter();
        out.print("<html>");
        out.print("<script>");
        out.print("window.location.href='"+request.getContextPath()+"/login.html'");
        out.print("</script>");
        out.print("</html>");
        return false;
    }
}
