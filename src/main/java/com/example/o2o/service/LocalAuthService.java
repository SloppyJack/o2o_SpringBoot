package com.example.o2o.service;

import com.example.o2o.entity.LocalAuth;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.service
 * @date: 2020/3/5 20:45
 **/
public interface LocalAuthService {
    /**
     * 通过userId查询本地用户
     * @param userId
     * @return
     */
    LocalAuth queryLocalAuthByUserId(Long userId);

    /**
     * 通过userName查询本地用户
     * @param userName
     * @return
     */
    LocalAuth queryLocalAuthByUserName(String userName);
}
