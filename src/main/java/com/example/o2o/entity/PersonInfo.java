package com.example.o2o.entity;

import java.util.Date;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.entity 普通用户
 * @date:2019/7/24
 **/
public class PersonInfo {
    //用户Id
    private Long userId;
    //用户名
    private String name;
    //用户头像
    private String headPortrait;
    //用户邮箱
    private String email;
    //用户性别
    private String sex;
    //用户类型 1=店家，2=顾客，3=超级管理员
    private Integer userType;
    //用户状态 1=正常，2=不可用
    private Integer userStatus;
    //创建时间
    private Date createTime;
    //最后修改时间
    private Date lastEditTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
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
