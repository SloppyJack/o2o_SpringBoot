package com.example.o2o.dao;

import com.example.o2o.entity.PersonInfo;
import org.springframework.stereotype.Repository;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.ssm.dao
 * @date: 2020/3/5 19:47
 **/
@Repository
public interface PersonInfoDao {

    /**
     * 根据userId查询用户
     * @param userId
     * @return
     */
    PersonInfo queryPersonInfoByUserId(Long userId);
}
