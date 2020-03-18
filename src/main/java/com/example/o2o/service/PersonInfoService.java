package com.example.o2o.service;

import com.example.o2o.entity.PersonInfo;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.service
 * @date: 2020/3/5 20:04
 **/
public interface PersonInfoService {

    PersonInfo queryPersonInfoByUserId(Long userId);
}
