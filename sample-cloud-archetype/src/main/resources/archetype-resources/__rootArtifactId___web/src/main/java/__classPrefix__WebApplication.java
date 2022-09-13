#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${groupId};

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("${package}.${rootArtifactId}.mapper")
public class ${classPrefix}WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(${classPrefix}WebApplication.class, args);
    }

}
