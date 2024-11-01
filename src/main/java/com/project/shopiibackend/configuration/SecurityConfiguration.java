package com.project.shopiibackend.configuration;

import com.project.shopiibackend.security.AuthenticationFilter;
import com.project.shopiibackend.security.SecurityEntryPoint;
import com.project.shopiibackend.service.IUserPrincipalService;
import com.project.shopiibackend.service.impl.UserPrincipalService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
@ComponentScan(basePackageClasses = {
        SecurityEntryPoint.class,
        IUserPrincipalService.class})
public class SecurityConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);

    private final UserPrincipalService userPrincipalService;

    private final SecurityEntryPoint securityEntryPoint;

    private final AuthenticationFilter authenticationFilter;

    @Autowired
    public void configure(AuthenticationManagerBuilder authenticationManager) throws Exception {
        authenticationManager.userDetailsService(userPrincipalService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        LOGGER.info("SecurityConfiguration invoked");
        httpSecurity.csrf(AbstractHttpConfigurer:: disable);

        httpSecurity
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.authorizeHttpRequests(
                req -> req.requestMatchers("/api/**", "/api/auth/register", "/api/auth/login", "/api/auth/logout").permitAll());

        httpSecurity.authorizeHttpRequests(
                req -> req.requestMatchers("/api/products/**").permitAll());

        httpSecurity.authorizeHttpRequests(
                req -> req.requestMatchers("/api/users/**", "/api/refresh-login").permitAll());

        httpSecurity.exceptionHandling(
                exception -> exception.authenticationEntryPoint(securityEntryPoint));

        httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.rememberMe(req -> req.tokenRepository(new InMemoryTokenRepositoryImpl()));

        return httpSecurity.build();
    }
}


//@Configuration
//@EnableWebSecurity
//@AllArgsConstructor
//@ComponentScan(basePackageClasses = {
//        SecurityEntryPoint.class,
//        IUserPrincipalService.class
//})
//public class SecurityConfiguration {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);
//
//    private final UserPrincipalService userPrincipalService;
//    private final SecurityEntryPoint securityEntryPoint;
//    private final AuthenticationFilter authenticationFilter;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(userPrincipalService)
//                .passwordEncoder(passwordEncoder())
//                .and()
//                .build();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        LOGGER.info("SecurityConfiguration invoked");
//
//        // Tắt CSRF vì đây là ứng dụng REST API
//        httpSecurity.csrf(AbstractHttpConfigurer::disable);
//
//        // Cấu hình chính sách session cho stateless
//        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        // Kích hoạt CORS
//        httpSecurity.cors(cors -> cors.configurationSource(request -> {
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Cho phép truy cập từ frontend
//            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
//            config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//            config.setAllowCredentials(true);
//            return config;
//        }));
//
//        // Định nghĩa các quy tắc phân quyền
//        httpSecurity.authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/logout", "/api/refresh-login").permitAll() // Cho phép truy cập các endpoint xác thực mà không cần token
////                .requestMatchers("/api/products/**").authenticated() // Các yêu cầu đến /api/products yêu cầu xác thực
//                        .requestMatchers("/api/products/**").permitAll()
//                        .requestMatchers("/api/brand/**").permitAll()
//                        .requestMatchers("/api/categories/**").permitAll()
//                        .requestMatchers("/api/color/**").permitAll()
//                        .requestMatchers("/api/size/**").permitAll()
//                        .requestMatchers("/api/subCategory/**").permitAll()
//                        .anyRequest().authenticated() // Các yêu cầu khác cần phải xác thực
//        );
//
//        // Xử lý lỗi xác thực và quyền truy cập
//        httpSecurity.exceptionHandling(customizer -> customizer.authenticationEntryPoint(securityEntryPoint));
//
//        // Thêm filter xác thực JWT trước UsernamePasswordAuthenticationFilter
//        httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        // Cấu hình Remember Me
//        httpSecurity.rememberMe(req -> req.tokenRepository(new InMemoryTokenRepositoryImpl()));
//
//        return httpSecurity.build();
//    }
//}
