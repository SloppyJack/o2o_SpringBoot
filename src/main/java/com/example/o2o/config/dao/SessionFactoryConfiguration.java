package com.example.o2o.config.dao;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.config.dao
 * @date: 2020/3/17 20:00
 **/
@Configuration
public class SessionFactoryConfiguration {
    @Autowired
    private DataSource dataSource;
    //mybatis-config.xml配置文件的路径

    private static String mybatisConfigFile = "mybatis-config.xml";
    //mybatis mapper的文件路径( **表示可以表示任意多级目录)

    private static String mapperPath = "/mapper/**.xml";
    //实体类所在的package
    @Value("${type_alias_package}")
    private String typeAliasPackage = "com.example.o2o.entity";

    @Value("${mybatis_config_file}")
    public void setMybatisConfigFile(String mybatisConfigFile) {
        SessionFactoryConfiguration.mybatisConfigFile = mybatisConfigFile;
    }

    @Value("${mapper_path}")
    public void setMapperPath(String mapperPath) {
        SessionFactoryConfiguration.mapperPath = mapperPath;
    }

    /**
     * 创建sessionFactoryBean实例，并设置configuration，
     * 设置mapper映射路径，设置dataSource数据源
     * @return
     */
    @Bean(name = "sqlSessionFactoryBean")
    public SqlSessionFactoryBean createSqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        //设置mybatis的扫描路径
        sessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisConfigFile));
        //添加mapper扫描路径
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + mapperPath;
        sessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(packageSearchPath));
        //设置DataSource
        sessionFactoryBean.setDataSource(dataSource);
        //设置typeAlias包扫描路径
        sessionFactoryBean.setTypeAliasesPackage(typeAliasPackage);
        return sessionFactoryBean;
    }
}
