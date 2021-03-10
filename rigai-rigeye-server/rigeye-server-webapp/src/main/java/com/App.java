package com;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.em.fx.apollox.ApolloReadConfig;
import com.em.fx.argus.tracking.mybatis.AddSpringbootInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;

/**
 * @author chenxing
 * Created by ChenXing on 2018/7/31.
 */

@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableApolloConfig
//@EnableCaching
@ComponentScan(basePackages = {
        "com.em"},excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,value = AddSpringbootInterceptor.class)
})
public class App {

    public static void main( String[] args )
    {
        ApolloReadConfig ap = new ApolloReadConfig();
        Map<String, Object> map = ap.getMapConfig("application,redis,jdbc,http,api,cas");
        SpringApplication app=new SpringApplication(App.class);
        app.setDefaultProperties(map);
        app.run(args);
    }
}
