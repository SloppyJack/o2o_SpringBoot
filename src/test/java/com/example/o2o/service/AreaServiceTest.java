package com.example.o2o.service;


import com.example.o2o.entity.Area;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.com.example.o2o.service
 * @date:2019/7/24
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaServiceTest{
    @Autowired
    private AreaService areaService;

    @Test
    public void testAreaService(){
        List<Area> areaList = areaService.getAreaList();
        for (Area temp:areaList){
            System.out.println(temp.getAreaName()+"--------------------------");
        }
        assertEquals(2,areaList.size());
    }
}
