package com.example.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.web.shopadmin
 * @date:2019/7/26
 **/
@Controller
@RequestMapping(value = "shopAdmin",method = {RequestMethod.GET})
public class ShopAdminController {
    /**
     * 路由到店铺操作页面
     * @return view
     */
    @RequestMapping(value = "/shopOperation")
    public String shopOperation(){
        return "pages/shop/shopOperation";
    }

    /**
     * 路由到商铺列表
     * @return view
     */
    @RequestMapping(value = "/shopList")
    public String shopList(){
        return "pages/shop/shopList";
    }

    /**
     * 返回到商铺管理界面
     * @return view
     */
    @RequestMapping(value = "/shopManagement")
    public String shopManagement(){
        return "pages/shop/shopManagement";
    }

    /**
     * 返回到商品管理界面
     * @return view
     */
    @RequestMapping(value = "/productManagement")
    public String productManagement(){
        return "pages/shop/productManagement";
    }

    /**
     * 商品分类界面
     * @return view
     */
    @RequestMapping(value = "/productCategoryManagement")
    public String productCategoryManagement(){
        return "pages/shop/productCategoryManagement";
    }

    @RequestMapping(value = "/productOperation")
    public String productOperation(){
        return "pages/shop/productOperation";
    }

    @RequestMapping(value = "/index")
    public String index(){
        return "pages/shop/index";
    }
}
