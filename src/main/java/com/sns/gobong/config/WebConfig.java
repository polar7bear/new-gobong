package com.sns.gobong.config;

import com.sns.gobong.util.api.RequestTimeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<RequestTimeFilter> requestTimeFilter() {
        FilterRegistrationBean<RequestTimeFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestTimeFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
