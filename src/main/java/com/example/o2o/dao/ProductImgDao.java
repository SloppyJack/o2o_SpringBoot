package com.example.o2o.dao;

import com.example.o2o.entity.ProductImg;

import java.util.List;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.ssm.dao
 * @date:2019/9/2
 **/
public interface ProductImgDao {

    /**
     * 批量添加商品图片
     * @param productImgList  图片列表
     * @return int
     */
    int batchInsertProductImg(List<ProductImg> productImgList);

    /**
     * 删除指定商品下的所有详情图
     * @param productId
     * @return
     */
    int deleteProductImgByProductId(long productId);

    /**
     * 根据productId找打商品详情图列表
     * @param productId
     * @return
     */
    List<ProductImg> queryProductImgList(long productId);
}
