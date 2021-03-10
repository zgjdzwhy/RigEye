package com.rigai;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.em.fx.apollox.ApolloReadConfig;
import com.em.fx.argus.tracking.Http.AddAsHttpFilter;
import com.em.fx.common.log.listener.ApolloListener;
import com.em.fx.common.log.logback.LogbackSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ExecutorService;


/**
 * Hello world!
 *
 * @author ChenXing
 */

@SpringBootApplication
@EnableTransactionManagement
@EnableApolloConfig
@ComponentScan(basePackages = "com.em", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = {AddAsHttpFilter.class})
})
public class App implements CommandLineRunner
{

    private Logger logger= LoggerFactory.getLogger(App.class);

    public static void main( String[] args )
    {
        initial();
        SpringApplication app = new SpringApplication(App.class);
        app.setBannerMode(Banner.Mode.OFF);
        ApolloReadConfig ap = new ApolloReadConfig();
        Map<String, Object> map = ap.getMapConfig("application,redis,jdbc,influx,http");
        app.setDefaultProperties(map);
        app.run(args);
    }


    @Autowired
    ExecutorService executorService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++");
        System.out.println("+++                      +++");
        System.out.println("+++      灯 等灯 等灯      +++");
        System.out.println("+++                      +++");
        System.out.println("++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++");
    }


    /**
     * 非web项目需要初始化logback
     */
    private static void initial(){
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        try {
            InputStream inStream = App.class.getResourceAsStream("/logback-spring.xml");
            configurator.doConfigure(inStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //动态日志
    @Bean
    public ApolloListener initListener(){
        LogbackSystem bootLoggingSystem=new LogbackSystem();
        return new ApolloListener(bootLoggingSystem);
    }
}
