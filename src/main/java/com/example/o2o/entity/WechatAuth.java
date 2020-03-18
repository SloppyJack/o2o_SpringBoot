package com.example.o2o.entity;

import java.util.Date;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.entity 微信端登录的用户
 * @date:2019/7/24
 **/
public class WechatAuth {
    //微信本身Id
    private Long wechatAuthId;
    //作为微信账号与公众号绑定唯一标示
    private String openId;
    //创建时间
    private Date createTime;
    //外键
    private PersonInfo personInfo;

    public Long getWechatAuthId() {
        return wechatAuthId;
    }

    public void setWechatAuthId(Long wechatAuthId) {
        this.wechatAuthId = wechatAuthId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }
}
