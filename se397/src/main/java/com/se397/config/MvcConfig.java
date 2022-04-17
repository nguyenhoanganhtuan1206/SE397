package com.se397.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path userUploadDir = Paths.get("../se397/src/main/resources/static/assets/img/product/products-photo/");
        String userUploadPath = userUploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/products-photo/**").addResourceLocations("file:/" + userUploadPath + "/");
    }
}
