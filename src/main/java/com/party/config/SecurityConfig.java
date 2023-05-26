package com.party.config;

import com.party.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http)throws Exception{


        http.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/", true)
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutUrl("/logout") // 로그아웃 처리 URL
                .invalidateHttpSession(true) // 세션 무효화 설정
                .deleteCookies("JSESSIONID") // 로그아웃 시 삭제할 쿠키 설정
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");





        http.authorizeRequests()
                .mvcMatchers("/","/members/**","/products/**","/product/**","/list/**","/images/**").permitAll()
                .mvcMatchers("/admin/**").hasAnyRole("ADMIN", "HOST")
                .mvcMatchers("/host/**").hasAnyRole("ADMIN", "HOST")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();





        http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }
    @Override
    public void configure(WebSecurity web)throws Exception{
        web.ignoring().antMatchers("/css/**","/js/**","/img/**","/error");

    }

    @Autowired
    MemberService memberService;

    //AuthenticationManagerBuilder 는  AuthenticationManager(인증관리자) 객체를 생성하는 역할
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }




}

