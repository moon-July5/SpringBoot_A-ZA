package com.moon.aza.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// MemberService와 SecurityConfig가 PasswordEncoder로 인해 서로 순환 참조되고 있기 때문에
// 그 원인이 되는 PasswordEncoder를 이렇게 따로 옮겨서 설정
@Configuration
public class AppConfig implements WebMvcConfigurer{
    @Value("${resource.handler}")
    private String resourceHandler;

    @Value("${resource.location}")
    private String resourceLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourceHandler)
                .addResourceLocations(resourceLocation);
    }

    /* 비밀번호 암호화 객체 */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
