package com.example.o2o.service.impl;

import com.example.o2o.dao.LocalAuthDao;
import com.example.o2o.entity.LocalAuth;
import com.example.o2o.service.LocalAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.service.impl
 * @date: 2020/3/5 20:46
 **/
@Service
public class LocalAuthServiceImpl implements LocalAuthService {
    @Autowired
    private LocalAuthDao localAuthDao;
    @Override
    public LocalAuth queryLocalAuthByUserId(Long userId) {
        return localAuthDao.queryLocalAuthByUserId(userId);
    }

    @Override
    public LocalAuth queryLocalAuthByUserName(String userName) {
        return localAuthDao.queryLocalAuthByUserName(userName);
    }
}
