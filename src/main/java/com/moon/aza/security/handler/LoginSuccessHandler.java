package com.moon.aza.security.handler;

import com.moon.aza.entity.User;
import com.moon.aza.security.dto.UserAuthDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        UserAuthDTO authDTO = (UserAuthDTO) authentication.getPrincipal();
        String certified = authDTO.getCertified();
        if(certified == "Y")
            redirectStrategy.sendRedirect(request, response, "/aza/index");
        else
            redirectStrategy.sendRedirect(request, response, "/user/emailAuth");

    }
}
