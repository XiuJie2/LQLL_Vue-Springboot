package com.example.springboot.common;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private JwtInterceptor jwtInterceptor;
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        //指定controller统一的接口前缀,相当于:在url上拼了一个/api/xxx
        configurer.addPathPrefix("/api", clazz -> clazz.isAnnotationPresent(RestController.class));
    }
    //加自定义拦截器JwtInterceptor,设置拦截规则
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login")
                .excludePathPatterns("/api/register")
                .excludePathPatterns("/api/product/selectAll")
                .excludePathPatterns("/api/files/**")
                .excludePathPatterns("/api/employee/export")
                .excludePathPatterns("/api/admin/export")
                .excludePathPatterns("/api/product/export")
                .excludePathPatterns("/api/employee/import")
                .excludePathPatterns("/api/admin/import")
                .excludePathPatterns("/api/product/import")
                .excludePathPatterns("/api/log/export")
        ;
    }
}