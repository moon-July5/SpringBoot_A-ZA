package com.moon.aza.config;

import com.moon.aza.security.dto.UserAuthDTO;
import com.moon.aza.security.handler.LoginFailureHandler;
import com.moon.aza.security.handler.LoginSuccessHandler;
import com.moon.aza.security.jwt.JwtAuthenticationFilter;
import com.moon.aza.security.jwt.JwtAuthenticationProvider;
import com.moon.aza.security.service.UserAuthService;
import com.moon.aza.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Log4j2
@RequiredArgsConstructor
@EnableWebSecurity // spring security 설정 클래스 선언
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserAuthService userAuthService;
    private final LoginFailureHandler loginFailureHandler;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    // 비밀번호 암호화 객체
    @Bean
    PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}

    // static 폴더의 하위 목록들 인증 무시
    @Override
    public void configure(WebSecurity web) throws Exception {
       web.ignoring().antMatchers("/css/**","/js/**","/font/**","/imgs/**");
    }

    // http 요청에 대한 웹 기반 보안
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()    // 접근 제한
                .antMatchers("/aza/*").permitAll() // 특정 경로 지정, 접근을 설정

        .and()
//                .formLogin()
//                .loginPage("/user/login")
//                .usernameParameter("id")
//                .passwordParameter("password")
//                .defaultSuccessUrl("/aza/index")
//                //.successHandler(successHandler())
//                .failureHandler(loginFailureHandler) // 로그인 실패 핸들러
//                .and()
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
//                .logoutSuccessUrl("/aza/index")
//                .invalidateHttpSession(true).deleteCookies("JSESSIONID")
//                .and()
                .rememberMe().tokenValiditySeconds(60*60*24).userDetailsService(userAuthService);

        http.httpBasic().disable()
                .cors().and()
                .csrf().disable()
                .formLogin().disable()
                // 토큰 기반 인증이므로 세션 사용 X
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationProvider), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userAuthService).passwordEncoder(passwordEncoder());
    }

    private LoginSuccessHandler successHandler(){return new LoginSuccessHandler();}
}
