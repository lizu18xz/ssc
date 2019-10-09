package com.fayayo.config;

import com.fayayo.filter.CrossDomainFilter;
import com.fayayo.filter.LoginFilter;
import com.fayayo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dalizu on 2019/9/26.
 * @version v1.0
 * @desc
 */
@Configuration
public class FilterConfiguration {

    @Autowired
    UserService userService;

    @Bean
    public FilterRegistrationBean loginFilterRegistration(){

        FilterRegistrationBean registrationBean=new FilterRegistrationBean(
                new LoginFilter(userService)
        );

        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("loginFilter");
        registrationBean.setOrder(2);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean crossDomainFilterRegistration(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(
                new CrossDomainFilter()
        );
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("crossDomainFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
