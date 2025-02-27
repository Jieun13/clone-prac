package me.jiny.prac220.controller;

import me.jiny.prac220.user.domain.User;
import me.jiny.prac220.user.dto.UserResponse;
import me.jiny.prac220.user.dto.UserUpdateRequest;
import org.springframework.http.ResponseEntity;
import me.jiny.prac220.user.config.TokenProvider;
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
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

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
        return ResponseEntity.ok(UserResponse.fromEntity(user));
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<UserResponse>> searchUser(@RequestParam String query) {
        List<User> users = userService.searchUsers(query);
        List<UserResponse> userResponses = users.stream().map(UserResponse::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }
}
