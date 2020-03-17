package com.example.o2o.entity;

import java.util.Date;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.ssm.entity
 * @date:2019/11/26
 **/
public class HeadNews {
    //Id
    private Integer newsId;
    //新闻名
    private String newsName;
    //新闻链接
    private String newsLink;
    //新闻图片
    private String newsImg;
    //权重
    private Integer weight;
    //状态 1 可用，0不可用
    private Integer status;
    //创建时间
    private Date createTime;
    //更新时间
    private Date lastEditTime;

    public HeadNews() {
    }

    public HeadNews(Integer newsId, String newsName, String newsLink, String newsImg, Integer weight, Integer status, Date createTime, Date lastEditTime) {
        this.newsId = newsId;
        this.newsName = newsName;
        this.newsLink = newsLink;
        this.newsImg = newsImg;
        this.weight = weight;
        this.status = status;
        this.createTime = createTime;
        this.lastEditTime = lastEditTime;
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getNewsName() {
        return newsName;
    }

    public void setNewsName(String newsName) {
        this.newsName = newsName;
    }

    public String getNewsLink() {
        return newsLink;
    }

    public void setNewsLink(String newsLink) {
        this.newsLink = newsLink;
    }

    public String getNewsImg() {
        return newsImg;
    }

    public void setNewsImg(String newsImg) {
        this.newsImg = newsImg;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
