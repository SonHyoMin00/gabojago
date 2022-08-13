package com.hanium.gabojago.config;

import com.hanium.gabojago.jwt.JwtAuthenticationFilter;
import com.hanium.gabojago.jwt.JwtTokenProvider;
import com.hanium.gabojago.jwt.UserDetailsServiceImpl;
import com.hanium.gabojago.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new UserDetailsServiceImpl(userRepository);
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authenticationConfiguration
//    ) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("--------------filter");
        log.info("--------------filter");
        log.info("--------------filter");

        http
                .authorizeRequests()
                .antMatchers("/hotplaces/**").permitAll()
                .antMatchers("/user/kakao/join").permitAll()
                .antMatchers("/user/kakao/login").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/user/kakao/callback").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
