package com.chan.labelimg.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: chen zheng
 * @Date: 12/6/2021 4:42 PM
 */
@Configuration
public class ResourceUrlMapping implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/BS/**").addResourceLocations("file:C:\\Users\\10721\\Desktop\\label-img\\label-img-vue\\public\\");
    }
}
