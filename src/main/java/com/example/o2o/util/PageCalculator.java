package com.example.o2o.util;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.util
 * @date:2019/8/26
 **/
public class PageCalculator {
    public static int calculateRowIndex(int pageIndex,int pageSize){
        //如果pageIndex大于0则换算，不然返回0
        return (pageIndex>0)?(pageIndex-1)*pageSize:0;
    }
}
