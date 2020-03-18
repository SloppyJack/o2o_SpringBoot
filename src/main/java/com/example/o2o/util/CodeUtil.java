package com.example.o2o.util;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.util
 * @date:2019/8/6
 **/
public class CodeUtil {
    public static boolean checkVerifyCode(HttpServletRequest request){
        HttpSession session =  request.getSession();
        String verifyCodeExpected = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        String verifyCodeActual = request.getParameter("verifyCodeActual");
        //不区分大小写
        if (verifyCodeActual==null||!verifyCodeActual.equalsIgnoreCase(verifyCodeExpected)){
            return false;
        }
        return true;
    }
}
