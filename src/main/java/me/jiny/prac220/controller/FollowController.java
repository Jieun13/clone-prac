package me.jiny.prac220.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import me.jiny.prac220.domain.Follow;
import me.jiny.prac220.dto.FollowResponse;
import me.jiny.prac220.service.FollowService;
import me.jiny.prac220.user.domain.User;
import me.jiny.prac220.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final UserService userService;

    @PostMapping("/users/{userId}/follow")
    public ResponseEntity<FollowResponse> create(@PathVariable Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User currentUser = userService.getCurrentUser();
        if(currentUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Follow follow = followService.create(currentUser.getId(), userId);
        return ResponseEntity.ok(FollowResponse.fromEntity(follow));
    }

    @GetMapping("/users/{userId}/follow")
    @Operation(summary = "팔로우 여부 조회", description = "특정 사용자를 팔로우 중인지 확인합니다.")
    public ResponseEntity<FollowResponse> getStatus(@PathVariable Long userId) {
        User currentUser = userService.getCurrentUser();
        Follow follow = followService.findByFollowerIdAndFollowingId(currentUser.getId(), userId);
        if(follow == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(FollowResponse.fromEntity(follow));
    }

    @GetMapping("/users/{userId}/followers")
    @Operation(summary = "팔로워 목록 조회", description = "특정 사용자의 팔로워 목록을 조회합니다.")
    public ResponseEntity<List<FollowResponse>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(
                followService.findAllFollowersByUserId(userId)
                        .stream()
                        .map(FollowResponse::fromEntity)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/users/{userId}/followings")
    @Operation(summary = "팔로잉 목록 조회", description = "특정 사용자가 팔로우하고 있는 유저 목록을 조회합니다.")
    public ResponseEntity<List<FollowResponse>> getFollowings(@PathVariable Long userId) {
        return ResponseEntity.ok(
                followService.findAllFollowingsByUserId(userId)
                        .stream()
                        .map(FollowResponse::fromEntity)
                        .collect(Collectors.toList())
        );
    }

    @DeleteMapping("/users/{userId}/follow")
    @Operation(summary = "팔로우 삭제", description = "현재 로그인한 사용자가 특정 사용자에 대한 팔로우를 취소합니다.")
    public ResponseEntity<FollowResponse> delete(@PathVariable Long userId) {
        followService.delete(userId, userService.getCurrentUser().getId());
        return ResponseEntity.noContent().build();
    }
}