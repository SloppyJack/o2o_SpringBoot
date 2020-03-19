package com.example.o2o.web.frontend;

import com.example.o2o.dto.CodeMsg;
import com.example.o2o.dto.Result;
import com.example.o2o.entity.HeadNews;
import com.example.o2o.entity.ShopCategory;
import com.example.o2o.service.HeadNewsService;
import com.example.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.web.frontend
 * @date:2019/11/27
 **/
@Controller
@RequestMapping("/frontend")
public class MainPageController {
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private HeadNewsService headNewsService;

    /**
     * 初始化前端系统的主页信息，包括获取一级店铺列表及头条列表
     * @return
     */
    @RequestMapping(value = "/listMainPageInfo",method = RequestMethod.GET)
    @ResponseBody
    private Result listMainPageInfo(){
        Map<String,Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList = new ArrayList<>();
        try {
            //获取一级店铺类别列表
            shopCategoryList = shopCategoryService.getShopCategoryList(null);
            modelMap.put("shopCategoryList",shopCategoryList);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_EXCEPTION,e.getMessage());
        }

        List<HeadNews> headNewsList = new ArrayList<>();
        try {
            //获取状态为可用的头条列表
            HeadNews headNews = new HeadNews();
            headNews.setStatus(1);
            headNewsList = headNewsService.getHeadNewsList(headNews);
            modelMap.put("headNewsList",headNewsList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_EXCEPTION,e.getMessage());
        }
        return Result.success(modelMap);
    }
}
