package com.example.o2o.config.web;

import com.example.o2o.interceptor.shopAdmin.ShopLoginInterceptor;
import com.example.o2o.interceptor.shopAdmin.ShopPermissionInterceptor;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.ServletException;

/**
 * @author: create by bin
 * @version: v1.0
 * @description: com.example.o2o.config.web
 * @date: 2020/3/19 19:52
 **/
@Configuration
public class MvcConfiguration extends WebMvcConfigurationSupport{
    @Value("${kaptcha.border}")
    private String border;

    @Value("${kaptcha.textproducer.font.color}")
    private String fColor;

    @Value("${kaptcha.image.width}")
    private String width;

    @Value("${kaptcha.textproducer.char.string}")
    private String cString;

    @Value("${kaptcha.image.height}")
    private String height;

    @Value("${kaptcha.textproducer.font.size}")
    private String fSize;

    @Value("${kaptcha.noise.color}")
    private String nColor;

    @Value("${kaptcha.textproducer.char.length}")
    private String cLength;

    @Value("${kaptcha.textproducer.font.names}")
    private String fName;

    /**
     * 创建ViewResolver，视图解析器
     * @return
     */
    @Bean(name = "viewResolver")
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    /**
     * 处理静态资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    /*@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }*/

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * 配置上传文件
     * @return
     */
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver createMultipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        //设置最大上传大小20m
        multipartResolver.setMaxUploadSize(1024*1024*20);
        multipartResolver.setMaxInMemorySize(1024*1024*20);
        return multipartResolver;
    }

    /**
     * 添加kaptcha验证码
     * @return
     */
    @Bean(name = "Kaptcha")
    public ServletRegistrationBean servletRegistrationBean(){
        //创建kaptchaServlet对象并且定义路由
        ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(),"/Kaptcha");
        servlet.addInitParameter("kaptcha:border",border);//无边框
        servlet.addInitParameter("kaptcha.textproducer.font.color",fColor);
        servlet.addInitParameter("kaptcha.image.width",width);
        servlet.addInitParameter("kaptcha.textproducer.char.string",cString);
        servlet.addInitParameter("kaptcha.image.height",height);
        servlet.addInitParameter("kaptcha.textproducer.font.size",fSize);
        servlet.addInitParameter("kaptcha.noise.color",nColor);
        servlet.addInitParameter("kaptcha.textproducer.char.length",cLength);
        servlet.addInitParameter("kaptcha.textproducer.font.names",fName);
        return servlet;
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        String interceptPath = "/shopAdmin/**";
        //注册拦截器
        InterceptorRegistration loginIR = registry.addInterceptor(new ShopLoginInterceptor());
        //配置拦截的路径
        loginIR.addPathPatterns(interceptPath);
        //继续注册其他的拦截器
        InterceptorRegistration permissionIR = registry.addInterceptor(new ShopPermissionInterceptor());
        //配置拦截的路径
        permissionIR.addPathPatterns(interceptPath);
        //配置不拦截的路径
        permissionIR.excludePathPatterns("/shopAdmin/index");
        permissionIR.excludePathPatterns("/shopAdmin/shopList");
        permissionIR.excludePathPatterns("/shopAdmin/getShopList");
        permissionIR.excludePathPatterns("/shopAdmin/getShopInitInfo");
        permissionIR.excludePathPatterns("/shopAdmin/registerShop");
        permissionIR.excludePathPatterns("/shopAdmin/shopOperation");
        permissionIR.excludePathPatterns("/shopAdmin/shopManagement");
        permissionIR.excludePathPatterns("/shopAdmin/getShopManagementInfo");
    }
}
