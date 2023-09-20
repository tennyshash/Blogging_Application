package com.shashwat.blog_app_youtube.Config;


import com.shashwat.blog_app_youtube.Security.CustomUserDetailService;
import com.shashwat.blog_app_youtube.Security.JwtAuthenticationEntryPoint;
import com.shashwat.blog_app_youtube.Security.JwtAuthenticationFilter;
import jakarta.servlet.FilterRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class SecurityConfig {

    private CustomUserDetailService customUserDetailService;
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(CustomUserDetailService customUserDetailService,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customUserDetailService = customUserDetailService;
        this.jwtAuthenticationEntryPoint=jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter=jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf( csrf -> csrf.disable())
                .cors( cors-> cors.disable())
                .authorizeHttpRequests( auth-> auth.requestMatchers(AppConstants.PUBLIC_URL).permitAll()
                        .requestMatchers(HttpMethod.GET).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling( ex-> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//         http .csrf( (csrf)-> csrf.disable() )
//                .authorizeHttpRequests( auth -> auth.requestMatchers(PUBLIC_URL).permitAll()
//                        .anyRequest().authenticated() )
//                .exceptionHandling( (exception)-> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
//                .sessionManagement( (session)-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//                .httpBasic(Customizer.withDefaults()) ;
//
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(daoAuthenticationProvider());

        return http.build();
    }


    /*  was used with WebSecurityConfigurerAdapter
    protected void Configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
    }

     */
    @Bean // used to implement basic auth from db
    public DaoAuthenticationProvider daoAuthenticationProvider(){

        DaoAuthenticationProvider provider= new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    /*
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }    */

    @Bean  // method used to authenticate user and pass  TODO: WHY Needed ?
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // to JOIN or Connect front end with backend
    @Bean
    public FilterRegistrationBean coresFilter(){

        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration= new CorsConfiguration();
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.addAllowedOriginPattern("*");
            corsConfiguration.addAllowedHeader("Authorization");
            corsConfiguration.addAllowedHeader("Accept");
            corsConfiguration.addAllowedHeader("Content-Type");
            corsConfiguration.addAllowedMethod("POST");
            corsConfiguration.addAllowedMethod("PUT");
            corsConfiguration.addAllowedMethod("GET");
            corsConfiguration.addAllowedMethod("DELETE");
            corsConfiguration.addAllowedMethod("OPTIONS");
            corsConfiguration.setMaxAge(3600L);

        source.registerCorsConfiguration("/**",corsConfiguration);

        FilterRegistrationBean filterRegistrationBean= new FilterRegistrationBean<>( new CorsFilter(source));

        filterRegistrationBean.setOrder(-110);
        return filterRegistrationBean;
    }

}
