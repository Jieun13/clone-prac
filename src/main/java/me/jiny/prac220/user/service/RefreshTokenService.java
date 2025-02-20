package me.jiny.prac220.user.service;

import me.jiny.prac220.user.domain.RefreshToken;
import me.jiny.prac220.user.config.TokenProvider;
import me.jiny.prac220.user.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token)
                .orElseThrow(()->new IllegalArgumentException("Unexpected User"));
    }

    @Transactional
    public void delete(){
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        Long userId = tokenProvider.getUserId(token);
        refreshTokenRepository.deleteByUserId(userId);
    }
}