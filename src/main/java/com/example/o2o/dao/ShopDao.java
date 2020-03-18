package com.example.o2o.dao;

import com.example.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.dao
 * @date:2019/7/25
 **/
@Repository
public interface ShopDao {
    /**
     *分页查询店铺（条件：店铺名【模糊】、店铺类别、区域Id、owner）
     * @param shopCondition 查询条件
     * @param rowIndex 从第几行开始取
     * @param pageSize 返回行数
     * @return
     */
    List<Shop> queryShopListOfPage(@Param("shopCondition") Shop shopCondition, @Param("rowIndex") int rowIndex,
                                   @Param("pageSize") int pageSize);

    /**
     * 返回店铺的总数
     * @param shopCondition 查询条件
     * @return int
     */
    int queryShopCountByShopCondition(@Param("shopCondition") Shop shopCondition);

    /**
     * 通过ShopId查询店铺信息
     * @param shopId shopId
     * @return
     */
    Shop queryByShopId(long shopId);

    /**
     * 新增店铺
     * @param shop shop
     * @return 返回为1代表成功
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     * @param shop shop
     * @return
     */
    int updateShop(Shop shop);
}
