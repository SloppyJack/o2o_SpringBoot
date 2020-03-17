package com.example.o2o.dao;

import com.example.o2o.entity.HeadNews;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.ssm.dao
 * @date:2019/11/26
 **/
@Repository
public interface HeadNewsDao {

    /**
     * 根据传入的条件查询头条
     * @param headNewsCondition
     * @return
     */
    List<HeadNews> queryHeadNews(@Param("headNewsCondition") HeadNews headNewsCondition);
}
