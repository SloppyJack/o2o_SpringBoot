package com.example.o2o.service.impl;

import com.example.o2o.dao.PersonInfoDao;
import com.example.o2o.entity.PersonInfo;
import com.example.o2o.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.service.impl
 * @date: 2020/3/5 20:04
 **/
@Service
public class PersonInfoServiceImpl implements PersonInfoService {
    @Autowired
    private PersonInfoDao personInfoDao;
    @Override
    public PersonInfo queryPersonInfoByUserId(Long userId) {
        return personInfoDao.queryPersonInfoByUserId(userId);
    }
}
