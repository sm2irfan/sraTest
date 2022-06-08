package com.optus.infosec.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
@EnableWebSecurity
public class ResourceServerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.oauth2ResourceServer(
                customizer -> customizer.jwt(
                        jwtConfigurer -> jwtConfigurer.decoder(decoder())
                )
        );

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().anyRequest().authenticated();

    }

    /*
    we specify how out resource server validate the JWT token
     */
    @Bean
    public JwtDecoder decoder() {
        String property = System.getProperty("sra.keycloak.jwk.token.store");
        return NimbusJwtDecoder.withJwkSetUri(property).build();
    }
}
