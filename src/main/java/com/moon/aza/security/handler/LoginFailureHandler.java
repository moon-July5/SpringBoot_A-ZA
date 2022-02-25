package com.moon.aza.security.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/*로그인 실패 핸들러*/
@Log4j2
@Component // Bean 등록
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {
        String message;
        if (exception instanceof BadCredentialsException) {
            message = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            message = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다.";
        } else if (exception instanceof UsernameNotFoundException) {
            message = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            message = "인증 요청이 거부되었습니다."; }
        else {
            message = "알 수 없는 이유로 로그인에 실패하였습니다.";
        }
        message = URLEncoder.encode(message, "UTF-8");

        setDefaultFailureUrl("/user/login?error=true&exception=" +message);
        log.info("/user/login?error=true&exception=" +message);
        super.onAuthenticationFailure(request, response, exception);
    }
}
