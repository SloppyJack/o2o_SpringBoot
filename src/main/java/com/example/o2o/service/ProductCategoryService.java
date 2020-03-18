package com.example.o2o.service;

import com.example.o2o.dto.ProductCategoryExecution;
import com.example.o2o.dto.Result;
import com.example.o2o.entity.ProductCategory;
import com.example.o2o.exceptions.ProductCategoryOperationException;

import java.util.List;

/** 商品类别Service
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.service
 * @date:2019/8/29
 **/
public interface ProductCategoryService {

    /**
     * 找到指定shopId的商品类别
     * @param shopId id
     * @return List<ProductCategory>
     */
    List<ProductCategory> getProductCategoryList(Long shopId);

    /**
     * 批量添加商品类别
     * @param productCategoryList 商品列表
     * @return Result
     * @throws ProductCategoryOperationException 异常信息
     */
    Result batchAddProductCategory(List<ProductCategory> productCategoryList)
        throws ProductCategoryOperationException;

    /**
     * 根据id来删除商品类别
     * @param productCategoryId id
     * @return 受影响的行数
     */
    ProductCategoryExecution deleteProductCategory(Long productCategoryId, long shopId);
}
