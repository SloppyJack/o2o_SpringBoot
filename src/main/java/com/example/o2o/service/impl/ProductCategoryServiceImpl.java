package com.example.o2o.service.impl;

import com.example.o2o.dao.ProductCategoryDao;
import com.example.o2o.dao.ProductDao;
import com.example.o2o.dto.CodeMsg;
import com.example.o2o.dto.ProductCategoryExecution;
import com.example.o2o.dto.Result;
import com.example.o2o.entity.ProductCategory;
import com.example.o2o.exceptions.ProductCategoryOperationException;
import com.example.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.service.impl
 * @date:2019/8/29
 **/
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public List<ProductCategory> getProductCategoryList(Long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }

    @Override
    @Transactional
    public Result batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException {
        if (productCategoryList!= null && productCategoryList.size()>0){
            try{
                int temp = productCategoryDao.batchInsertProductCategory(productCategoryList);
                if (temp <= 0){
                    throw new ProductCategoryOperationException("商品类别创建失败！");
                }else {
                    return Result.success(CodeMsg.PRODUCT_CATEGORY_CREATE_SUCCESS);
                }
            }catch (Exception e){
                return Result.error(CodeMsg.SERVER_EXCEPTION);
            }
        }else {
            return Result.error(CodeMsg.PRODUCT_CATEGORY_EMPTY_LIST);
        }
    }

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(Long productCategoryId,long shopId)
        throws ProductCategoryOperationException{
        //解除tb_product中的商品与该productCategoryId的关联
        try{
            int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
            if (effectedNum < 0)
                throw new RuntimeException("商品类别更新失败");
        }catch (Exception e){
            throw new RuntimeException("删除商品类别错误 ："+ e.getMessage());
        }
        //删除该productCategory
        try{
            int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId,shopId);
            if (effectedNum <= 0){
                throw new ProductCategoryOperationException("商品类别删除失败");
            }else {
                return new ProductCategoryExecution(CodeMsg.PRODUCT_CATEGORY_DEL_SUCCESS);
            }
        }catch (Exception e){
            throw new ProductCategoryOperationException("删除商品类别错误："+e.getMessage());
        }
    }
}
