package com.hwx.backeend.config;

import com.hwx.backeend.security.ExceptionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


        @Autowired
        ExceptionFilter exceptionFilter;



}
