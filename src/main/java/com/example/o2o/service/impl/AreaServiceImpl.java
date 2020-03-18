package com.example.o2o.service.impl;

import com.example.o2o.dao.AreaDao;
import com.example.o2o.entity.Area;
import com.example.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.service.impl
 * @date:2019/7/24
 **/
@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaDao areaDao;

    @Override
    public List<Area> getAreaList() {
        return areaDao.queryArea();
    }
}
