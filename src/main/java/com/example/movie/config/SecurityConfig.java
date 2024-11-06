package com.example.movie.config;

import com.example.movie.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private static final String[] PUBLIC_LIST = {"/auth/login","/auth/logout","/auth/register","/actor/public/**", "/category/public/**", "/movie/public/**"};
    private static final String[] PRIVATE_LIST = {"actor/system/**","/category/system/**","/movie/system/**"};
    private static final String[] PROTECTED_LIST = {"/movie/create"};

    @Value("${movie.private}")
    private String PRIVATE;

    @Value("${movie.public}")
    private String PUBLIC;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(c -> c.disable())
                .authorizeHttpRequests(
                        authz -> authz
                                .requestMatchers(PUBLIC_LIST).permitAll()
                                .requestMatchers(PRIVATE_LIST).hasAnyAuthority(PRIVATE)
                                .requestMatchers(PROTECTED_LIST).hasAnyAuthority(PUBLIC)
                                .anyRequest().authenticated())
                .formLogin(f -> f.disable())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/**"); // Bỏ qua các tài nguyên tĩnh như CSS, JS
    }

}
