package com.example.o2o.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: 对比example.o2o框架中的spring-service中的transactionManager，
 * 继承TransactionManagementConfigurer是因为开启annotation-driven(配置基于注解的声明式事务)
 * @date: 2020/3/18 19:05
 **/
@Configuration
@EnableTransactionManagement    //此标签用于开启事务支持
public class TransactionManagementConfiguration implements TransactionManagementConfigurer {
    @Autowired
    private DataSource dataSource;


    /**
     * 关于事务管理，须返回PlatformTransactionManager的实例
     * @return
     */
    @Override
    public TransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
