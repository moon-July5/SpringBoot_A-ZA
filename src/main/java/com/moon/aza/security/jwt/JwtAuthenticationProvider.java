package com.moon.aza.security.jwt;

import com.moon.aza.security.service.UserAuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/* JWT를 생성, 검증, 정보 추출해주는 클래스 */
@RequiredArgsConstructor
public class JwtAuthenticationProvider {
    private String secretKey = "secret";
    private long tokenValidTime = 1000L * 60 * 60; // 유효시간 60분

    private final UserAuthService userAuthService;

    public String createToken(String userPK, List<String> roles){
        // JWT payload에 저장되는 정보 단위
        Claims claims = Jwts.claims().setSubject(userPK);
        claims.put("roles",roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now)  // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey) // signature에 들어갈 secret 값 세팅
                .compact();
    }
    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token){
        UserDetails userDetails = userAuthService.loadUserByUsername(this.getUserPK(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }
    // 토큰에서 회원 정보 추출
    public String getUserPK(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token).getBody().getSubject();
    }
    // Request의 Header에서 token 값을 가져옵니다.
    // "X-AUTH-TOKEN" : "TOKEN 값"
    public String resolveToken(HttpServletRequest request){
        String token = null;
        Cookie cookie = WebUtils.getCookie(request,"X-AUTH-TOKEN");
        if(cookie != null) token = cookie.getValue();
        return token;
    }
    // 토큰의 유효성 + 만료일자 확인
    public boolean validDateToken(String jwtToken){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e){
            return false;
        }
    }
}
