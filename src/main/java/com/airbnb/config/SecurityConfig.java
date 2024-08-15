package com.airbnb.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {

    private JWTFilter jwtFilter;

    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        //h(cd)2
        http.csrf().disable().cors().disable();


        //haap
       // http.authorizeHttpRequests().anyRequest().permitAll();

//        http.addFilterBefore(jwtFilter, AuthorizationFilter.class);
//        http.authorizeHttpRequests()
//                .requestMatchers("/api/v1/auth/**")
//                .permitAll()
//                .anyRequest().authenticated();

        http.authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/createUser","/api/v1/auth/createpropertyowner","/api/v1/auth/login")
                .permitAll() // Public endpoints
                .requestMatchers("/api/v1/auth/createpropertymanager").hasRole("ADMIN")
                .requestMatchers("/api/v1/auth/addproperty").hasAnyRole("ADMIN","OWNER","MANAGER")
                .anyRequest().authenticated(); // All other requests need authentication

        // Add JWT filter before AuthorizationFilter
        http.addFilterBefore(jwtFilter, AuthorizationFilter.class);

        return http.build();
    }
}
