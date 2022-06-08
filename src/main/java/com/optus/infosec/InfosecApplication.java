package com.optus.infosec;

import com.optus.infosec.api.service.InfosecUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication( exclude = {SecurityAutoConfiguration.class,BatchAutoConfiguration.class})@EnableSwagger2
public class InfosecApplication extends SpringBootServletInitializer implements CommandLineRunner {

    @Autowired
    InfosecUsersService infosecUsersService;

    public static void main(String[] args) {
        SpringApplication.run(InfosecApplication.class, args);
    }

    @Bean(name = "infosecReadinessApprovalCertificate")
    public ClassPathResource getViewResolver() {
        return new ClassPathResource("InfosecReadinessApprovalCertificate.html");
    }

    @Override
    public void run(String... args) {
        infosecUsersService.getUsersFromKeycloak();
    }
}
