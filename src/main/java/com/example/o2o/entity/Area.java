package com.example.o2o.entity;

import java.util.Date;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.entity
 * @date:2019/7/24
 **/
public class Area {
    //ID
    private Integer areaId;
    //区域名
    private String areaName;
    //权重
    private Integer weight;
    //创建时间
    private Date createTime;
    //更新时间
    private Date lastEditTime;

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }
}
