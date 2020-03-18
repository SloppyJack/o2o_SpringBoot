package com.example.o2o.dto;

import com.example.o2o.entity.Shop;

import java.util.List;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.dto 店铺执行类，只要操作店铺类Service层都会返回这个
 * @date:2019/7/25
 **/
public class ShopExecution {
    //结果状态
    private int state;
    //状态标识
    private String stateInfo;
    //店铺数量
    private int count;
    //操作的shop
    private Shop shop;
    //shop列表（查询店铺列表使用）
    private List<Shop> shopList;

    public ShopExecution(){

    }

    //店铺操作失败所使用的的构造器
    public ShopExecution(CodeMsg codeMsg){
        this.state = codeMsg.getRetCode();
        this.stateInfo = codeMsg.getMessage();
    }

    //店铺操作成功使用的构造器
    public ShopExecution(CodeMsg codeMsg,Shop shop){
        this.state = codeMsg.getRetCode();
        this.stateInfo = codeMsg.getMessage();
        this.shop  = shop;
    }
    //店铺操作成功使用的构造器（返回一个店铺列表）
    public ShopExecution(CodeMsg codeMsg,List<Shop> shopList){
        this.state = codeMsg.getRetCode();
        this.stateInfo = codeMsg.getMessage();
        this.shopList = shopList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
