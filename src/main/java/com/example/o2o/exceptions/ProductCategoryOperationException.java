package com.example.o2o.exceptions;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: 商铺列表操作异常消息
 * @date:2019/8/1
 **/
public class ProductCategoryOperationException extends RuntimeException {

    public ProductCategoryOperationException(String msg){
        super(msg);
    }
}
