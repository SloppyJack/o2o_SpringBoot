package com.example.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.o2o.dto.CodeMsg;
import com.example.o2o.dto.ImageHolder;
import com.example.o2o.dto.ProductExecution;
import com.example.o2o.dto.Result;
import com.example.o2o.entity.Product;
import com.example.o2o.entity.ProductCategory;
import com.example.o2o.entity.Shop;
import com.example.o2o.exceptions.ProductOperationException;
import com.example.o2o.service.ProductCategoryService;
import com.example.o2o.service.ProductService;
import com.example.o2o.util.CodeUtil;
import com.example.o2o.util.HttpServletRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
 * @description: com.example.o2o.web.shopadmin
 * @date:2019/10/13
 **/
@Controller
@RequestMapping("/shopAdmin")
public class ProductManagementController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    //支持上传商品详情图的最大数量
    private static final int IMAGE_MAX_COUNT = 6;

    @RequestMapping(value = "/addProduct",method = RequestMethod.POST)
    @ResponseBody
    private Result addProduct(HttpServletRequest request){
        //验证码校验
        if (!CodeUtil.checkVerifyCode(request)){
            return Result.error(CodeMsg.VERIFYCODE_ERROR);
        }
        //接收前端参数的变量初始化，包括商品，缩略图，详情图列表实体类
        ObjectMapper mapper = new ObjectMapper();
        Product product;
        String productStr = HttpServletRequestUtil.getString(request,"productStr");
        //ImageHolder用来处理缩略图
        ImageHolder thumbnail;
        //List用来处理商品详情图列表
        List<ImageHolder> productImgList = new ArrayList<>();
        //从session中获取文件流
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext()
        );
        try{
            if (multipartResolver.isMultipart(request)){
                thumbnail = handleImage((MultipartHttpServletRequest) request, productImgList);
            }else {
                return Result.error(CodeMsg.UPLOAD_IMAGE_ILLEGAL);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_EXCEPTION);
        }
        //若果缩略图或缩略图为空，则返回
        if (thumbnail == null || productImgList == null)
            return Result.error(CodeMsg.UPLOAD_IMAGE_ILLEGAL);

        try{
            //尝试获取前端传过来的表单string流并将其转换成Product实体类
            product = mapper.readValue(productStr,Product.class);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_EXCEPTION);
        }

        //若Product信息，缩略图及其详情列表为非空，则开始商品添加操作
        if (product!=null&&thumbnail.getImage()!=null&&productImgList.size()>0){
            try {
                //从session中获取当前店铺的Id并赋值给Product,减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                Shop shop = new Shop();
                shop.setShopId(currentShop.getShopId());
                product.setShop(shop);
                //执行添加操作
                ProductExecution pe = productService.addProduct(product,thumbnail,productImgList);
                if (pe.getState() == CodeMsg.PRODUCT_ADD_SUCCESS.getRetCode()){
                    return Result.success();
                }else {
                    return Result.error(CodeMsg.ERROR,pe.getStateInfo());
                }
            } catch (ProductOperationException e) {
                e.printStackTrace();
                return Result.error(CodeMsg.SERVER_EXCEPTION);
            }
        }else {
            return Result.error(CodeMsg.PRODUCT_IS_NULL);
        }
    }

    @RequestMapping(value = "/getProductById",method = RequestMethod.GET)
    @ResponseBody
    private Result getProductById(@RequestParam Long productId){
        Map<String,Object> modelMap = new HashMap<>();
        //非空判断
        if (productId != null){
            //获取商品信息
            Product product = productService.getProductById(productId);
            if (product == null)
                return Result.error(CodeMsg.PRODUCT_IS_NULL);
            //获取该店铺下的商品类别列表
            List<ProductCategory> productCategoryList = productCategoryService
                    .getProductCategoryList(product.getShop().getShopId());
            modelMap.put("product",product);
            modelMap.put("productCategoryList",productCategoryList);
            return Result.success(modelMap);
        }else {
            return Result.error(CodeMsg.PRODUCTID_IS_NULL);
        }
    }

    @RequestMapping(value = "/modifyProduct",method = RequestMethod.POST)
    @ResponseBody
    private Result modifyProduct(HttpServletRequest request){
        //判断操作为编辑或上架操作
        boolean statusChange = HttpServletRequestUtil.getBoolean(request,"statusChange");
        //验证码判断
        if (!statusChange && !CodeUtil.checkVerifyCode(request))
            return Result.error(CodeMsg.VERIFYCODE_ERROR);
        //接收前端参数的变量的初始化，包括商品、缩略图、详情图列表
        ObjectMapper mapper = new ObjectMapper();
        Product product;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        //处理文件上传
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getServletContext());
        //若请求中存在文件流，则取出相关的文件（缩略图、详情图）
        try {
            if (multipartResolver.isMultipart(request)){
                thumbnail = handleImage((MultipartHttpServletRequest) request,productImgList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_EXCEPTION);
        }
        try {
            String productStr = HttpServletRequestUtil.getString(request,"productStr");
            //尝试获取前段闯过来的表单String并转换成Product实体类
            if (StringUtils.isBlank(productStr))
                return Result.error(CodeMsg.PARAMETER_ILLEGAL);
            product = mapper.readValue(productStr,Product.class);
        }catch (Exception e){
            return Result.error(CodeMsg.SERVER_EXCEPTION);
        }
        if (product ==null)
            return Result.error(CodeMsg.PRODUCT_IS_NULL);
        try {
            //从session中获取当前店铺的id并赋值给product，减少对前台数据依赖
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            Shop shop = new Shop();
            shop.setShopId(currentShop.getShopId());
            product.setShop(currentShop);
            //开始对商品变更进行操作
            ProductExecution pe = productService.modifyProduct(product,thumbnail,productImgList);
            if (pe.getState() == CodeMsg.PRODUCT_UPDATE_SUCCESS.getRetCode()){
                return Result.success();
            }else {
                return Result.error(CodeMsg.FAILED,pe.getStateInfo());
            }
        }catch (Exception e){
            return Result.error(CodeMsg.SERVER_EXCEPTION);
        }
    }

    @RequestMapping(value = "/getProductListByShop",method = RequestMethod.POST)
    @ResponseBody
    private Result getProductListByShop(HttpServletRequest request){
        //获取前台传过来的页码
        int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
        //获取前台传过来的列表大小
        int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
        //从当前session中获取店铺信息（shopId）
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值判断
        if (pageIndex > -1 && pageSize > -1  && currentShop != null && currentShop.getShopId() != null){
            //获取传入的需要检索的条件
            //筛选的条件
            long productCategoryId = HttpServletRequestUtil.getLong(request,"productCategoryId");
            String productName = HttpServletRequestUtil.getString(request,"productName");
            Product productCondition = compactProductCondition(currentShop.getShopId(),productCategoryId,productName);
            //传入查询条件以及分页信息进行查询，返回相应商品列表及总数
            ProductExecution pe = productService.getProductList(productCondition,pageIndex,pageSize);
            Map<String,Object> modelMap = new HashMap<>();
            modelMap.put("productList",pe.getProductList());
            modelMap.put("count",pe.getCount());
            return Result.success(modelMap);
        }else {
            return Result.error(CodeMsg.FAILED);
        }
    }

    //将前台传过来的值转换为缩略图和图片详情列表
    private ImageHolder handleImage(MultipartHttpServletRequest request, List<ImageHolder> productImgList) throws IOException {
        ImageHolder thumbnail = null;
        //取出缩略图，并构建ImageHolder对象
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) request.getFile("thumbnail");
        if(thumbnailFile != null){
            thumbnail = new ImageHolder(thumbnailFile.getInputStream(),thumbnailFile.getOriginalFilename());
        }
        //取出详情图列表，并构建List<ImageHolder>列表对象，最多支持六张图片上传
        for (int i = 0;i < IMAGE_MAX_COUNT;i++){
            CommonsMultipartFile productImgFile = (CommonsMultipartFile) request.getFile("productImg"+i);
            if (productImgFile != null){
                //取出的第i个详情图文件流不为空，则加入详情图列表
                ImageHolder productImg = new ImageHolder(productImgFile.getInputStream(),productImgFile.getOriginalFilename());
                productImgList.add(productImg);
            }else {
                //若取出的第i个详情图文件流为空，终止循环
                break;
            }
        }
        return thumbnail;
    }

    public static Product compactProductCondition(Long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        //若有指定类别的要求则添加进去
        if (productCategoryId != -1L){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        //若商品名不为空
        if (StringUtils.isNotEmpty(productName)){
            productCondition.setProductName(productName);
        }
        productCondition.setStatus(CodeMsg.PRODUCT_STATUS_ONLINE.getRetCode());
        return productCondition;
    }
}
