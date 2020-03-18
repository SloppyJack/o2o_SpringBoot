package com.example.o2o.service;

import com.example.o2o.dto.ImageHolder;
import com.example.o2o.dto.ProductExecution;
import com.example.o2o.entity.Product;
import com.example.o2o.exceptions.ProductOperationException;

import java.util.List;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: 商品操作Service层
 * @date:2019/10/8
 **/
public interface ProductService {

    /**
     *添加商品信息及图片处理
     * @param product 操作的商品
     * @param thumbnail 缩略图对象
     * @param productImgList 商品图片列表对象
     * @return ProductExecution
     * @throws ProductOperationException 商品操作异常
     */
    ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException;

    /**
     * 通过商品Id查询唯一的商品信息
     * @param productId
     * @return
     */
    Product getProductById(long productId);

    /**
     * 修改商品以及图片处理
     * @param product
     * @param imageHolder
     * @param productImages
     * @return
     * @throws ProductOperationException
     */
    ProductExecution modifyProduct(Product product, ImageHolder imageHolder, List<ImageHolder> productImages) throws
            ProductOperationException;

    /**
     * 根据查询条件，获取商品列表（支持productName模糊查询）
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

    /**
     * 根据条件获取商品总数
     * @param productCondition
     * @return
     */
    int getProductCount(Product productCondition);
}
