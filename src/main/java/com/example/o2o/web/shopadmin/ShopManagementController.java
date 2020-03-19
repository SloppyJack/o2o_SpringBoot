package com.example.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.o2o.dto.CodeMsg;
import com.example.o2o.dto.ImageHolder;
import com.example.o2o.dto.Result;
import com.example.o2o.dto.ShopExecution;
import com.example.o2o.entity.Area;
import com.example.o2o.entity.PersonInfo;
import com.example.o2o.entity.Shop;
import com.example.o2o.entity.ShopCategory;
import com.example.o2o.service.AreaService;
import com.example.o2o.service.ShopCategoryService;
import com.example.o2o.service.ShopService;
import com.example.o2o.util.CodeUtil;
import com.example.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.web.shopadmin 实现店铺管理相关的逻辑
 * @date:2019/7/25
 **/
@Controller
@RequestMapping("/shopAdmin")
public class ShopManagementController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/getAllShopCategory",method = RequestMethod.GET)
    @ResponseBody
    private Result getAllShopCategory( ){
        try {
            return Result.success(shopCategoryService.getShopCategoryList(null));
        }catch (Exception e){
            return Result.error(CodeMsg.ERROR,e.getMessage());
        }
    }

    /**
     * 判断用户是否有权限直接访问店铺页面
     * @param request 请求
     * @return Result
     */
    @RequestMapping(value = "/getShopManagementInfo",method = RequestMethod.GET)
    @ResponseBody
    private Result getShopManagementInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<String,Object>();
        long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        if (shopId<=0){
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if (currentShopObj == null){
                modelMap.put("redirect",true);
                modelMap.put("url","/example.o2o_war_exploded/shopAdmin/shopList");
            }else {
                Shop currentShop = (Shop)currentShopObj;
                modelMap.put("redirect",false);
                modelMap.put("shopId",currentShop.getShopId());
            }
        }else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop",currentShop);
            modelMap.put("redirect",false);
        }
        return Result.success(modelMap);
    }

    /**
     * 得到当前登录用户的商铺列表
      * @param request 请求
     * @return Result
     */
    @RequestMapping(value = "/getShopList",method = RequestMethod.GET)
    @ResponseBody
    private Result getShopList(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap();
        PersonInfo user = (PersonInfo)request.getSession().getAttribute("user");
        if (user == null || user.getUserId()== null)
            return Result.error(CodeMsg.NULL_SHOPLIST);
        try{
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution se = shopService.getShopListOfPage(shopCondition,0,100);
            //列出店铺成功之后，将店铺放入session作为权限验证
            request.getSession().setAttribute("shopList",se.getShopList());
            modelMap.put("shopList",se.getShopList());
            modelMap.put("user",user);
            return Result.success(modelMap);
        }catch (Exception e){
            return Result.error(CodeMsg.FAILED,e.getMessage());
        }
    }


    /**
     * 根据id得到指定的shop
     * @param request 请求
     * @return Result结果类
     */
    @RequestMapping(value = "/getShopById",method = RequestMethod.GET)
    @ResponseBody
    private Result getShopById(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap();
        Long shopId = HttpServletRequestUtil.getLong(request,"shopId");
        if (shopId > -1){
            try {
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                return Result.success(modelMap);
            }catch (Exception e){
                return Result.error(CodeMsg.ERROR,e.getMessage());
            }
        }else {
            return Result.error(CodeMsg.FAILED);
        }
    }

    /**
     * 得到店铺初始化区域列表和分类信息
     * @return Result结果类
     */
    @RequestMapping(value = "/getShopInfo",method = RequestMethod.GET)
    @ResponseBody
    private Result getShopInitInfo(){
        Map<String,Object> modelMap = new HashMap<>();
        try {
            modelMap.put("shopCategoryList",shopCategoryService.getShopCategoryList(new ShopCategory()));
            modelMap.put("areaList",areaService.getAreaList());
            return Result.success(modelMap);
        }catch (Exception e){
            return Result.error(CodeMsg.SERVER_EXCEPTION);
        }
    }

    /**
     * 更改店铺信息
     * @param request 请求
     * @return Result
     */
    @SuppressWarnings("Duplicates")
    @RequestMapping(value = "/modifyShop",method = RequestMethod.POST)
    @ResponseBody
    private Result modifyShop(HttpServletRequest request){
        if (!CodeUtil.checkVerifyCode(request)){
            return Result.error(CodeMsg.VERIFYCODE_ERROR);
        }
        //1、接收并转化相应的参数，包括店铺信息以及图片信息
        String shopStr= HttpServletRequestUtil.getString(request,"shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop;
        try {
            shop = mapper.readValue(shopStr,Shop.class);
        }catch (Exception e){
            return Result.error(CodeMsg.ERROR,e.getMessage());
        }
        //获取前端传过来的文件流
        CommonsMultipartFile shopImg = null;
        //CommonsMultipartResolver文件上传解析器
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext()
        );
        //判断是否有上传的文件流
        if (commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
            shopImg =(CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        //2、修改店铺
        if (shop!=null && shop.getShopId() != null){
            ShopExecution se;
            try {
                ImageHolder imageHolder = new ImageHolder(shopImg.getInputStream(),shopImg.getOriginalFilename());
                if (shopImg == null){
                    se = shopService.modifyShop(shop,null);
                }else {
                    se = shopService.modifyShop(shop,imageHolder);
                }
                if (se.getState()== CodeMsg.SHOP_OPERATION_SUCCESS.getRetCode()){
                    return Result.success();
                }else {
                    return Result.error(CodeMsg.FAILED,se.getStateInfo());
                }
            } catch (IOException e) {
                return Result.error(CodeMsg.SERVER_EXCEPTION);
            }
        }else {
            return Result.error(CodeMsg.NULL_SHOP_ID);
        }
    }

    /**
     * 注册店铺
     * @param request 客户端请求
     * @return 返回map键值对
     */
    @SuppressWarnings({"Duplicates", "unchecked"})
    @RequestMapping(value = "/registerShop",method = RequestMethod.POST)
    @ResponseBody
    private Result registerShop(HttpServletRequest request){
        Result result;
        if (!CodeUtil.checkVerifyCode(request)){
            return Result.error(CodeMsg.VERIFYCODE_ERROR);
        }
        //1、接收并转化相应的参数，包括店铺信息以及图片信息
        String shopStr= HttpServletRequestUtil.getString(request,"shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop;
        try {
            //
            shop = mapper.readValue(shopStr,Shop.class);
        }catch (Exception e){
            return Result.error(CodeMsg.SERVER_EXCEPTION);
        }
        //获取前端传过来的文件流
        CommonsMultipartFile shopImg;
        //CommonsMultipartResolver文件上传解析器
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext()
        );
        //判断是否有上传的文件流
        if (commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
            shopImg =(CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }else {
            return Result.error(CodeMsg.UPLOAD_IMAGE_ILLEGAL);
        }
        //2、注册店铺
        if (shop!=null && shopImg !=null){
            PersonInfo owner = (PersonInfo)request.getSession().getAttribute("user");
            shop.setOwner(owner);
            ShopExecution se;
            try {
                ImageHolder imageHolder = new ImageHolder(shopImg.getInputStream(),shopImg.getOriginalFilename());
                se = shopService.addShop(shop,imageHolder);
                if (se.getState()== CodeMsg.SHOP_STATUS_CHECK.getRetCode()){
                    result =  Result.success();
                    //该用户可以操作的店铺列表
                    List<Shop> shopList = (List<Shop>)request.getSession().getAttribute("shopList");
                    if (shopList ==null || shopList.size() == 0){
                        shopList = new ArrayList<Shop>();
                    }
                    shopList.add(se.getShop());
                    request.getSession().setAttribute("shopList",shopList);
                }else {
                    result =  Result.error(CodeMsg.FAILED,se.getStateInfo());
                }
            } catch (IOException e) {
                result = Result.error(CodeMsg.SERVER_EXCEPTION);
            }
            return result;
        }else {
            return Result.error(CodeMsg.FAILED,"请输入店铺信息");
        }
    }

}
