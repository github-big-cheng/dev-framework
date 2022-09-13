#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${parentArtifactId}.common;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class ${classPrefix}WebConfig {


    @Bean
    public ApplicationListener<ContextRefreshedEvent> applicationListener() {
        // 容器刷新后
        return applicationEvent -> {

        };
    }

}
