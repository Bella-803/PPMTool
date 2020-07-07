package com.learnspringboot.ppmtool;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    // registry.addMapping("/api/project/**")
    // .allowedOrigins("http://localhost:8080/")
    // .allowedMethods("GET","POST")
    // .allowedHeaders("header1","header2","header3")
    // .exposedHeaders("header1","header2");

    // }
}
