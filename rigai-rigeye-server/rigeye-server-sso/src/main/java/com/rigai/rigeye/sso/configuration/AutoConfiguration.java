package com.rigai.rigeye.sso.configuration;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/10/12.
 */

@Configuration
public class AutoConfiguration {

    @Value("${casServerLoginUrl}")
    private String casServerLoginUrl;
    @Value("${casServerUrlPrefix}")
    private String casServerUrlPrefix;
    @Value("${casServerLogoutUrl}")
    private String casServerLogoutUrl;
    @Value("${serverNameInCas}")
    private String serverNameInCas;
    @Value("${gateWay}")
    private String gateWay;
    //cas filter begin
    @Bean
    public FilterRegistrationBean casSingleSignOutFilterRegistration() {
        SingleSignOutFilter singleSignOutFilter=new SingleSignOutFilter();
        FilterRegistrationBean registration =new FilterRegistrationBean(singleSignOutFilter);
        registration.setOrder(2);
        registration.addInitParameter("casServerUrlPrefix",casServerUrlPrefix);
        String[] gateWays=gateWay.split(",");
        for(String way:gateWays){
            registration.addUrlPatterns(way);
        }
        return registration;
    }

    @Bean
    public FilterRegistrationBean casFilterRegistration() {
        AuthenticationFilter casFilter=new AuthenticationFilter();
        FilterRegistrationBean registration =new FilterRegistrationBean(casFilter);
        registration.setOrder(3);
        registration.addInitParameter("casServerLoginUrl",casServerLoginUrl);
        System.out.println(serverNameInCas);
        registration.addInitParameter("serverName",serverNameInCas);
        String[] gateWays=gateWay.split(",");
        for(String way:gateWays){
            registration.addUrlPatterns(way);
        }
        return registration;
    }

    @Bean
    public FilterRegistrationBean casValidationFilterRegistration() {
        Cas20ProxyReceivingTicketValidationFilter validationFilter=new Cas20ProxyReceivingTicketValidationFilter();
        FilterRegistrationBean registration =new FilterRegistrationBean(validationFilter);
        registration.setOrder(4);
        registration.addInitParameter("casServerUrlPrefix",casServerUrlPrefix);
        registration.addInitParameter("serverName",serverNameInCas);
        String[] gateWays=gateWay.split(",");
        for(String way:gateWays){
            registration.addUrlPatterns(way);
        }
        return registration;
    }

    @Bean
    public FilterRegistrationBean wrapperFilterRegistration() {
        HttpServletRequestWrapperFilter wrapperFilter=new HttpServletRequestWrapperFilter();
        FilterRegistrationBean registration =new FilterRegistrationBean(wrapperFilter);
        registration.setOrder(5);
        String[] gateWays=gateWay.split(",");
        for(String way:gateWays){
            registration.addUrlPatterns(way);
        }
        return registration;
    }

    @Bean
    public FilterRegistrationBean casAssertionFilterRegistration() {
        AssertionThreadLocalFilter assertionFilter=new AssertionThreadLocalFilter();
        FilterRegistrationBean registration =new FilterRegistrationBean(assertionFilter);
        registration.setOrder(6);
        String[] gateWays=gateWay.split(",");
        for(String way:gateWays){
            registration.addUrlPatterns(way);
        }
        return registration;
    }
}
