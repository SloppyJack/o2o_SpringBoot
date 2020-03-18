package com.example.o2o.dao;

import com.example.o2o.entity.Area;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.dao
 * @date:2019/7/24
 **/
@Repository
public interface AreaDao {
    /**
     * 列出区域列表
     * @return areaList
     */
    List<Area> queryArea();
}
