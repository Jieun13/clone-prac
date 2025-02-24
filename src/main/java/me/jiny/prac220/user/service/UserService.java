package me.jiny.prac220.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.jiny.prac220.user.dto.UserRequest;
import me.jiny.prac220.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import me.jiny.prac220.user.domain.User;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long save(UserRequest userRequest) {
        return userRepository.save(User.builder()
                .username(userRequest.getUsername())
                .password(bCryptPasswordEncoder.encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .build()).getId();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Unexpected User"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("Unexpected User"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("사용자가 인증되지 않음");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("Unexpected User"));
    }

    @Transactional
    public User updateNickname(Long id, String nickname) {
        User user = findById(id);
        user.updateNickname(nickname);
        return userRepository.save(user);
    }
}