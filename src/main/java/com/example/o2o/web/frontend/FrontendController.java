package com.example.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.web.frontend
 * @date:2019/11/30
 **/
@Controller
@RequestMapping(value = "/frontend",method = {RequestMethod.GET})
public class FrontendController {

    @RequestMapping(value = "/homePage")
    public String homePage(){
        return "pages/frontend/homePage";
    }

    @RequestMapping(value = "/shopList")
    public String shopList(){
        return "pages/frontend/shopList";
    }

    @RequestMapping(value = "/shopDetail")
    public String shopDetail(){
        return "pages/frontend/shopDetail";
    }
}
