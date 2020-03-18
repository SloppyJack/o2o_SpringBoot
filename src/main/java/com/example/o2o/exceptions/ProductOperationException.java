package com.example.o2o.exceptions;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: 商铺操作异常消息
 * @date:2019/10/8
 **/
public class ProductOperationException extends RuntimeException{
    public ProductOperationException(String msg){
        super(msg);
    }
}
