package me.jiny.prac220.user.controller;

import me.jiny.prac220.user.domain.User;
import me.jiny.prac220.user.config.CookieUtil;
import me.jiny.prac220.user.dto.CreateAccessTokenRequest;
import me.jiny.prac220.user.dto.CreateAccessTokenResponse;
import me.jiny.prac220.user.config.TokenProvider;
import me.jiny.prac220.user.dto.UserResponse;
import me.jiny.prac220.user.dto.UserUpdateRequest;
import me.jiny.prac220.user.service.TokenService;
import me.jiny.prac220.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserApiController {

    private final TokenService tokenService;
    private final TokenProvider tokenProvider;
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, "jwt_token");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<User> profile(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // "Bearer {token}"에서 실제 토큰만 추출
        String token = authorizationHeader.substring(7);
        if (token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            Long userId = tokenProvider.getUserId(token);
            User user = userService.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // 잘못된 토큰 처리
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(UserResponse.fromEntity(user));
    }

    @PostMapping("/users/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
        User user = userService.findById(userId);
        if (!user.equals(userService.getCurrentUser())){
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        userService.updateNickname(userId, request.getNickname());
        System.out.println("이름 변경 요청 : " + request.getNickname() + " " + user.getNickname());
        return ResponseEntity.ok(UserResponse.fromEntity(user));
    }
}