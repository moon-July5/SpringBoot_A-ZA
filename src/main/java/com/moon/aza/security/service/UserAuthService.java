package com.moon.aza.security.service;

import com.moon.aza.entity.User;
import com.moon.aza.repository.UserRepository;
import com.moon.aza.security.dto.UserAuthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserAuthService implements UserDetailsService {
    private final UserRepository userRepository;
    // 로그인을 위한 유저 정보 조회
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User result = userRepository.findById(id);
        return new UserAuthDTO(result);
    }
}
