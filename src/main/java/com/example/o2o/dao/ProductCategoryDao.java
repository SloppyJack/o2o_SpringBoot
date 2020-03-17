package com.example.o2o.dao;

import com.example.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.ssm.dao
 * @date:2019/8/28
 **/
@Repository
public interface ProductCategoryDao {

    /**
     * 通过shopId查询店铺商铺类别
     * @param shopId 商铺Id
     * @return List<ProductCategory>
     */
    List<ProductCategory> queryProductCategoryList(long shopId);

    /**
     * 批量更新商品类别
     * @param productCategoryList 商品类别列表
     * @return 影响行数
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    /**
     * 根据Id删除指定的商品类别
     * @param productCategoryId
     * @return
     */
    int deleteProductCategory(@Param("productCategoryId") Long productCategoryId, @Param("shopId") Long shopId);
}
