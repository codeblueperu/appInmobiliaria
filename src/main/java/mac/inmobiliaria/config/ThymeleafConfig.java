package mac.inmobiliaria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.dialect.springdata.SpringDataDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
public class ThymeleafConfig {


    @Bean
    public SpringDataDialect springDataDialect()throws RuntimeException {
        return new SpringDataDialect();
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect()throws RuntimeException{
        return new SpringSecurityDialect();
    }
}
