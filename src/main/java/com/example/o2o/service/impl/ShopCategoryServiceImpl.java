package com.example.o2o.service.impl;

import com.example.o2o.dao.ShopCategoryDao;
import com.example.o2o.entity.ShopCategory;
import com.example.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.service.impl
 * @date:2019/8/5
 **/
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategory) {
        return shopCategoryDao.queryShopCategory(shopCategory);
    }
}
