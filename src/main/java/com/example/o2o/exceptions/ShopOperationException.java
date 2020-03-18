package com.example.o2o.exceptions;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: 商品操作异常消息
 * @date:2019/8/1
 **/
public class ShopOperationException extends RuntimeException {

    public ShopOperationException(String msg){
        super(msg);
    }
}
