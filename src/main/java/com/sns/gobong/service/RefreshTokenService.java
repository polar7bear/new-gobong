package com.sns.gobong.service;

import com.sns.gobong.domain.entity.RefreshToken;
import com.sns.gobong.domain.entity.User;
import com.sns.gobong.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveRefreshToken(User user, String refreshToken) {
        RefreshToken entity = new RefreshToken(refreshToken, user);
        refreshTokenRepository.save(entity);
    }
}
