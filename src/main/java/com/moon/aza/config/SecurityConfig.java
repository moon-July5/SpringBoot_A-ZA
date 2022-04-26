package com.moon.aza.config;

import com.moon.aza.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Log4j2
@RequiredArgsConstructor
@EnableWebSecurity // spring security 설정 클래스 선언
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final MemberService memberService;
    private final DataSource dataSource;

    // static 폴더의 하위 목록들 인증 무시
    @Override
    public void configure(WebSecurity web) throws Exception {
       web.ignoring().antMatchers("/css/**","/js/**","/font/**","/imgs/**");
    }

    // http 요청에 대한 웹 기반 보안
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()    // 접근 제한
                .antMatchers("/","/board","/login","/signup",
                        "/member/email-check-token","/member/email-login",
                        "/member/login-by-email"
                        ).permitAll(); // 특정 경로 지정, 접근을 설정
        http.formLogin()
                .loginPage("/login")
                .permitAll();
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/");
        http.rememberMe()
                .userDetailsService(memberService)
                .tokenRepository(tokenRepository());
        // X-Frame-Options Click jacking 공격 막기 설정
        http.headers()
                .frameOptions().sameOrigin();
        // 'ckeditor4' 이미지 업로드 시 403 Forbidden Error로 인해 설정
        http.cors().and()
                .csrf().disable();
    }
    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

}
