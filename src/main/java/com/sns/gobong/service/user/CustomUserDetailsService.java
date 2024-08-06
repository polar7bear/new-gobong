package com.sns.gobong.service.user;

import com.sns.gobong.config.security.CustomUserDetails;
import com.sns.gobong.domain.entity.User;
import com.sns.gobong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + ": DB에 존재하지 않는 유저입니다."));
        return new CustomUserDetails(user.getId(), user.getEmail(), user.getPw(), new ArrayList<>()); // TODO: 권한 설정은 어떻게 할 지?
    }
}
