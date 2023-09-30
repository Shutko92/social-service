package ru.skillbox.diplom.group42.social.service.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import ru.skillbox.diplom.group42.social.service.security.jwt.JwtTokenFilter;
import ru.skillbox.diplom.group42.social.service.security.jwt.JwtTokenProvider;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    @Value("${endpoints.admin}")
    private String ADMIN_ENDPOINT;

    @Value("#{'${endpoints.public}'.split(', ')}")
    private  String[] publicEndpoints;

    @Value("${roles.admin}")
    public static final String ROLE = "admin";

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    @Bean
    protected LogoutSuccessHandler successLogout() {
        return (HttpServletRequest, HttpServletResponse, Authentication) -> HttpServletResponse.setStatus(javax.servlet.http.HttpServletResponse.SC_OK);
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .cors()
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(publicEndpoints).permitAll()
                .antMatchers(ADMIN_ENDPOINT).hasRole(ROLE)
                .anyRequest().authenticated()
                .and()
                .logout()
                .deleteCookies("jwt")
                .logoutSuccessHandler(successLogout())
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
