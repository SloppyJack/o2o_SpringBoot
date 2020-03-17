package com.example.o2o.dao;

import com.example.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.ssm.dao
 * @date:2019/9/2
 **/
public interface ProductDao {

    /**
     * 添加商品
     * @param product 商品
     * @return int
     */
    int insertProduct(Product product);

    /**
     * 通过productId查询唯一的商品信息
     * @param productId 商品id
     * @return
     */
    Product queryProductById(long productId);

    /**
     * 更新商品信息
     * @param product
     * @return
     */
    int updateProduct(Product product);

    /**
     * 根据条件获取商品列表
     * @param productCondition 查询条件
     * @param rowIndex 起始行
     * @param pageSize 分页大小
     * @return
     */
    List<Product> queryProductList(@Param("productCondition") Product productCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 根据条件查询商品数量
     * @param productCondition 查询条件
     * @return
     */
    int queryProductCount(@Param("productCondition") Product productCondition);

    /**
     * 将所有该类别的商品类别置为null
     * @param productCategoryId
     * @return
     */
    int updateProductCategoryToNull(long productCategoryId);
}
