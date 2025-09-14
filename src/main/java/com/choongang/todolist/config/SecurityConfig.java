package com.choongang.todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable()
        );
        http.sessionManagement(sessionManagementConfigurer ->
                sessionManagementConfigurer.maximumSessions(100)
                        .maxSessionsPreventsLogin(false));
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated());
        http.formLogin(formLoginConfigurer ->
                formLoginConfigurer.loginPage("/login")
                        .loginProcessingUrl("/login_proc")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/todos", true));
        http.logout(logoutConfigurer -> {
            logoutConfigurer.logoutSuccessUrl("/login")
                    .logoutUrl("/logout").permitAll();
        });
        http.userDetailsService(userDetailsService);

        return http.build();
    }
}
