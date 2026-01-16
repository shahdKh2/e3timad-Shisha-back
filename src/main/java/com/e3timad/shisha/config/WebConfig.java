package com.e3timad.shisha.config;

import com.e3timad.shisha.security.JwtFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@Configuration
public class WebConfig {


    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new JwtFilter());
        reg.addUrlPatterns("/api/*");
        return reg;
    }
}
