package me.jiny.prac220.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.jiny.prac220.user.domain.User;
import me.jiny.prac220.user.dto.UserResponse;
import me.jiny.prac220.user.dto.UserUpdateRequest;
import org.springframework.http.ResponseEntity;
import me.jiny.prac220.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Tag(name = "프로필 API", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @Operation(summary = "사용자 조회", description = "사용자 ID로 특정 사용자의 정보를 조회합니다.")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(UserResponse.fromEntity(user));
    }

    @PostMapping("/{userId}")
    @Operation(summary = "사용자 정보 수정", description = "현재 로그인한 사용자가 자신의 닉네임을 변경합니다.")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
        User user = userService.findById(userId);
        if (!user.equals(userService.getCurrentUser())){
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        userService.updateNickname(userId, request.getNickname());
        return ResponseEntity.ok(UserResponse.fromEntity(user));
    }

    @GetMapping("/search")
    @Operation(summary = "사용자 검색", description = "닉네임을 기준으로 사용자를 검색합니다.")
    public ResponseEntity<List<UserResponse>> searchUser(@RequestParam String query) {
        List<User> users = userService.searchUsers(query);
        List<UserResponse> userResponses = users.stream().map(UserResponse::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }
}
