package com.sns.gobong.config;

import com.sns.gobong.util.api.RequestTimeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<RequestTimeFilter> requestTimeFilter() {
        FilterRegistrationBean<RequestTimeFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestTimeFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
