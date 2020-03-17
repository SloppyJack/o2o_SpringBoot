package com.example.o2o.config.dao;

import com.example.o2o.util.DESUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.config.dao
 * @date: 2020/3/17 19:44
 **/
@Configuration
@MapperScan("com.example.o2o.dao")  //配置mybatis mapper的扫描路径
public class DataSourceConfiguration {
    @Value("${jdbc.driver}")    //value标签会从application.properties中读取出相应的变量
    private String jdbcDriver;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbcPassword;

    /**
     * 生成与spring-data.xml对应的bean dataSource
     * @return
     */
    @Bean(name = "dataSource")
    public ComboPooledDataSource createDataSource() throws PropertyVetoException {
        //生成DataSource的实例
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        //设置相应的配置信息
        dataSource.setDriverClass(jdbcDriver);
        dataSource.setJdbcUrl(jdbcUrl);
        //利用工具类对登录名及密码进行解密
        dataSource.setUser(DESUtils.getDecryptString(jdbcUsername));
        dataSource.setPassword(DESUtils.getDecryptString(jdbcPassword));
        //配置c3p0连接池的私有属性
        dataSource.setMaxPoolSize(30);
        dataSource.setMinPoolSize(10);
        dataSource.setAutoCommitOnClose(false);
        dataSource.setCheckoutTimeout(10000);
        dataSource.setAcquireRetryAttempts(2);
        return dataSource;
    }
}
