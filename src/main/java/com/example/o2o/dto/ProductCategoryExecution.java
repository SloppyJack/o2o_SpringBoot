package com.example.o2o.dto;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.dto
 * @date:2019/8/29
 **/
public class ProductCategoryExecution {
    //状态码
    private int state;
    //状态标识
    private String stateInfo;

    public ProductCategoryExecution(){}

    //构造器
    public ProductCategoryExecution(CodeMsg codeMsg){
        this.state = codeMsg.getRetCode();
        this.stateInfo = codeMsg.getMessage();
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
