package com.example.o2o.service;

import com.example.o2o.entity.ShopCategory;

import java.util.List;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.service
 * @date:2019/8/5
 **/
public interface ShopCategoryService {

    /**
     * 根据查询条件获取店铺分类列表
     * @param shopCategory
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategory);
}
