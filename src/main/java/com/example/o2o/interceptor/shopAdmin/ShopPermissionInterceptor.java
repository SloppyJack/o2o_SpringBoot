package com.example.o2o.interceptor.shopAdmin;

import com.example.o2o.entity.Shop;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.ssm.interceptor.shopAdmin
 * @date: 2020/3/11 22:03
 **/
public class ShopPermissionInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从Session中取出当前的店铺
        Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
        List<Shop> shopList = (List<Shop>)request.getSession().getAttribute("shopList");
        //非空判断
        if (currentShop != null && shopList != null){
            for (Shop shop : shopList){
                if (shop.getShopId() == currentShop.getShopId()){
                    return true;
                }
            }
        }
        //若不满足拦截器验证，则返回false，终止用户的执行
        return false;
    }
}
