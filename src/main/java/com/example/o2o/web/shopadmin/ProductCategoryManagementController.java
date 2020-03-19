package com.example.o2o.web.shopadmin;

import com.example.o2o.dto.CodeMsg;
import com.example.o2o.dto.Result;
import com.example.o2o.entity.ProductCategory;
import com.example.o2o.entity.Shop;
import com.example.o2o.exceptions.ProductCategoryOperationException;
import com.example.o2o.service.ProductCategoryService;
import com.example.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**商品类别管理
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.web.shopadmin
 * @date:2019/8/29
 **/
@Controller
@RequestMapping("/shopAdmin")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping(value = "/deleteProductCategory",method = RequestMethod.POST)
    @ResponseBody
    private Result deleteProductCategory(HttpServletRequest request){
        Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
        Long productCategoryId = HttpServletRequestUtil.getLong(request,"id");
        if (productCategoryId<=0){
            return Result.error(CodeMsg.PARAMETER_ILLEGAL);
        }
        try{
            if (productCategoryService.deleteProductCategory(productCategoryId,currentShop.getShopId()).getState()
                    == CodeMsg.PRODUCT_CATEGORY_DEL_SUCCESS.getRetCode()){
                return Result.success();
            }else {
                return Result.error(CodeMsg.FAILED);
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_EXCEPTION);
        }
    }

    /**
     * 根据ShopId得到商品分类列表
     * @param request 请求
     * @return 返回Result
     */
    @RequestMapping(value = "/getProductCategoryList",method = RequestMethod.GET)
    @ResponseBody
    private Result getProductCategoryList(HttpServletRequest request){
        Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
        if (currentShop != null && currentShop.getShopId()>0)
            return Result.success(productCategoryService.getProductCategoryList(currentShop.getShopId()));
        else
            return Result.error(CodeMsg.SERVER_EXCEPTION);
    }


    /**
     * 批量添加商品分类
     * @param productCategoryList 分类列表
     * @param request 请求
     * @return Result
     */
    @RequestMapping(value = "/addProductCategorys",method = RequestMethod.POST)
    @ResponseBody
    private Result addProductCategorys(@RequestBody List<ProductCategory> productCategoryList, HttpServletRequest request){
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        for (ProductCategory pc:productCategoryList){
            pc.setShopId(currentShop.getShopId());
            pc.setCreateTime(new Date());
        }
        if (productCategoryList.size() > 0){
            try {
                Result temp = productCategoryService.batchAddProductCategory(productCategoryList);
                if (temp.getRetCode() == CodeMsg.PRODUCT_CATEGORY_CREATE_SUCCESS.getRetCode()){
                    return Result.success();
                }else {
                    return Result.error(CodeMsg.FAILED,temp.getMessage());
                }
            }catch (ProductCategoryOperationException e){
                return Result.error(CodeMsg.FAILED);
            }
        }else {
            return Result.error(CodeMsg.PRODUCT_INPUT_ATLEAST_ONE);
        }
    }
}
