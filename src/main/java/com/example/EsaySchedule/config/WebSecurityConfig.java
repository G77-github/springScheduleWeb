package com.example.EsaySchedule.config;

import com.example.EsaySchedule.service.UserDetailService;
import com.example.EsaySchedule.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailService userService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .requestMatchers("/static/**");
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .authorizeRequests()
//                .requestMatchers("/login", "/signup", "/user").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .successHandler(customAuthenticationSuccessHandler)
//                .defaultSuccessUrl("/main")
//                .and()
//                .logout()
//                .logoutSuccessUrl("/login")
//                .invalidateHttpSession(true)
//                .and()
//                .csrf().disable()
//                .build();
//    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((request) ->
                        request.requestMatchers("/login", "/signup", "/checkUserEmail", "/css/**").permitAll()
                                .anyRequest().authenticated())
                .formLogin( (formLogin) ->
                        formLogin.loginPage("/login")
                                .defaultSuccessUrl("/main"))
                .csrf( (csrf) -> csrf.disable());

        return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity
            , BCryptPasswordEncoder bCryptPasswordEncoder
            , UserDetailService userDetailService) throws Exception{
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
