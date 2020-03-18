package com.example.o2o.dto;

import com.example.o2o.entity.Product;

import java.util.List;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.dto
 * @date:2019/10/8
 **/
public class ProductExecution {
    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    //商品数量
    private int count;

    //操作的product（增删改商品的时候用）
    private Product product;

    //获取product列表(查询商品列表的时候使用)
    private List<Product> productList;

    //空的构造方法
    public ProductExecution(){}

    //失败的构造器
    public  ProductExecution(CodeMsg codeMsg){
        this.state = codeMsg.getRetCode();
        this.stateInfo = codeMsg.getMessage();
    }

    //成功的构造器(单个商品)
    public ProductExecution(CodeMsg codeMsg,Product product){
        this.state = codeMsg.getRetCode();
        this.stateInfo = codeMsg.getMessage();
        this.product = product;
    }

    //成功的构造器(商品列表)
    public ProductExecution(CodeMsg codeMsg,List<Product> productList){
        this.state = codeMsg.getRetCode();
        this.stateInfo = codeMsg.getMessage();
        this.productList = productList;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
