package com.sns.gobong.service;

import com.sns.gobong.config.security.jwt.TokenProvider;
import com.sns.gobong.domain.dto.request.UserSignOutRequestDto;
import com.sns.gobong.domain.entity.RefreshToken;
import com.sns.gobong.domain.entity.User;
import com.sns.gobong.exception.user.InvalidRefreshTokenException;
import com.sns.gobong.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public void saveRefreshToken(User user, String refreshToken) {
        RefreshToken entity = new RefreshToken(refreshToken, user);
        refreshTokenRepository.save(entity);
    }

    @Transactional
    public String deleteRefreshToken(UserSignOutRequestDto dto) {
        String requestRefreshToken = dto.getRefreshToken();
        boolean result = tokenProvider.validationRefreshToken(requestRefreshToken);
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(dto.getRefreshToken())
                .orElseThrow(() -> new InvalidRefreshTokenException("유효하지 않는 리프레시 토큰입니다."));

        String ogRefreshToken = refreshToken.getRefreshToken();
        if (result && requestRefreshToken.equals(ogRefreshToken)) {
            refreshTokenRepository.delete(refreshToken);
            SecurityContextHolder.clearContext();

            return "로그아웃이 성공적으로 완료되었습니다.";
        } else {
            throw new InvalidRefreshTokenException("유효하지 않은 리프레시 토큰입니다.");
        }
    }
}
