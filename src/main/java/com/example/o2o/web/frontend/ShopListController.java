package com.example.o2o.web.frontend;

import com.example.o2o.dto.CodeMsg;
import com.example.o2o.dto.Result;
import com.example.o2o.dto.ShopExecution;
import com.example.o2o.entity.Area;
import com.example.o2o.entity.Shop;
import com.example.o2o.entity.ShopCategory;
import com.example.o2o.service.AreaService;
import com.example.o2o.service.ShopCategoryService;
import com.example.o2o.service.ShopService;
import com.example.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.web.frontend
 * @date:2019/12/17
 **/
@Controller
@RequestMapping("/frontend")
public class ShopListController {
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopService shopService;

    /**
     * 返回商品列表里的ShopCategory列表（二级或一级）
     * @param request
     * @return
     */
    @RequestMapping(value = "/listShopPageInfo",method = RequestMethod.GET)
    @ResponseBody
    private Result listShopPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取parentId
        long parentId = HttpServletRequestUtil.getLong(request,"parentId");
        List<ShopCategory> shopCategoryList;
        if (parentId != -1){
            //如果parentId存在，则取出该一级shopCategory下的耳机ShpCategory列表
            try{
                ShopCategory shopCategoryCondtion = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategoryCondtion.setParent(parent);
                shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondtion);
            }catch (Exception e){
                e.printStackTrace();
                return Result.error(CodeMsg.SERVER_EXCEPTION);
            }
        }else {
            try{
                //若果parentId不存在，则取出所有以及shopCategory（全部商店列表）
                shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            }catch (Exception e){
                e.printStackTrace();
                return Result.error(CodeMsg.SERVER_EXCEPTION);
            }
        }
        modelMap.put("shopCategoryList",shopCategoryList);
        List<Area> areaList;
        try{
            //获取区域列表信息
            areaList = areaService.getAreaList();
            modelMap.put("areaList",areaList);
        }catch (Exception e){
            return Result.error(CodeMsg.SERVER_EXCEPTION);
        }
        return Result.success(modelMap);
    }

    /**
     * 获取指定查询条件下的店铺列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/listShops",method = RequestMethod.GET)
    @ResponseBody
    private Result listShops(HttpServletRequest request){
        //获取页码
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        //获取一页需要显示的数据条数
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        //非空判断
        if (pageIndex> -1 && pageSize > -1){
            //获取一级类别Id
            long parentId = HttpServletRequestUtil.getLong(request,"parentId");
            //获取二级类别Id
            long shopCategoryId = HttpServletRequestUtil.getLong(request,"shopCategoryId");
            //获取区域Id
            int areaId = HttpServletRequestUtil.getInt(request,"areaId");
            //获取模糊查询的名字
            String shopName = HttpServletRequestUtil.getString(request,"shopName");
            //获取组合之后的查询条件
            Shop shopCondition = compactShopCondition(parentId,shopCategoryId,areaId,shopName);
            //根据查询条件和分页信息获取店铺列表，并返回总数
            ShopExecution se = shopService.getShopListOfPage(shopCondition,pageIndex,pageSize);
            return Result.success(se);
        }else {
            return Result.error(CodeMsg.EMPTY_PAGESIZE_OR_PAGEINDEX);
        }
    }

    /**
     * 组合查询条件
     * @param parentId
     * @param shopCategoryId
     * @param areaId
     * @param shopName
     * @return
     */
    private Shop compactShopCondition(long parentId, long shopCategoryId, int areaId, String shopName) {
        Shop shopCondition = new Shop();
        if (parentId != -1L){
            //查询一级ShopCategory下面的所有二级ShopCategory里的店铺列表
            ShopCategory childCategory = new ShopCategory();
            ShopCategory parentCategory = new ShopCategory();
            //设置父类别的类别Id
            parentCategory.setShopCategoryId(parentId);
            //设置子类别的父节点
            childCategory.setParent(parentCategory);
            //设置店铺查询条件
            shopCondition.setShopCategory(childCategory);
        }
        if (shopCategoryId != -1L){
            //查询二级ShopCategory下面的店铺列表
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if (areaId != -1L){
            //查询位于某个区域Id下的店铺列表
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        if (shopName != null){
            //查询名字中含有shopName的店铺列表
            shopCondition.setShopName(shopName);
        }
        //前端展示的店铺都是营业中的店铺
        shopCondition.setStatus(CodeMsg.SHOP_STATUS_PASS.getRetCode());
        return shopCondition;
    }
}
