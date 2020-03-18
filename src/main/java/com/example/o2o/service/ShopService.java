package com.example.o2o.service;

import com.example.o2o.dto.ImageHolder;
import com.example.o2o.dto.ShopExecution;
import com.example.o2o.entity.Shop;
import com.example.o2o.exceptions.ShopOperationException;


/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.service
 * @date:2019/7/25
 **/
public interface ShopService {

    /**
     * 根据ShopCondition得到店铺列表
     * @param shopCondition 查询条件
     * @param pageIndex 起始
     * @param pageSize 分页大小
     * @return ShopExecution
     */
    ShopExecution getShopListOfPage(Shop shopCondition, int pageIndex, int pageSize);

    /**
     * 通过店铺Id获取店铺信息
     * @param shopId 商铺Id
     * @return Shop
     */
    Shop getByShopId(long shopId);

    /**
     * 更新店铺信息，包括图片的处理
     * @param shop 店铺类
     * @param imageHolder 图片持有者对象
     * @return ShopExecution
     * @throws ShopOperationException 商铺操作异常信息
     */
    ShopExecution modifyShop(Shop shop, ImageHolder imageHolder)
            throws ShopOperationException;

    /**
     * 添加店铺
     * @param shop 商铺
     * @param imageHolder 图片持有者对象
     * @return ShopExecution
     */
    ShopExecution addShop(Shop shop, ImageHolder imageHolder);
}
