package me.jiny.prac220.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import me.jiny.prac220.domain.Repost;
import me.jiny.prac220.dto.RepostResponse;
import me.jiny.prac220.service.RepostService;
import me.jiny.prac220.user.domain.User;
import me.jiny.prac220.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RepostController {

    private final RepostService repostService;
    private final UserService userService;

    @PostMapping("/posts/{postId}/reposts")
    @Operation(summary = "포스트 리포스트", description = "특정 포스트를 리포스트한다.")
    public ResponseEntity<RepostResponse> createRepost(@PathVariable Long postId) {
        User user = userService.getCurrentUser();
        Repost repost = repostService.create(user, postId);
        return ResponseEntity.ok(RepostResponse.fromEntity(repost));
    }

    @GetMapping("/posts/{postId}/repost")
    @Operation(summary = "리포스트 여부 조회", description = "특정 리포스트 여부를 가져온다.")
    public ResponseEntity<RepostResponse> isReposted(@PathVariable Long postId) {
        User user = userService.getCurrentUser();
        Repost repost = repostService.getRepostByPostIdAndUser(postId, user);
        if (repost == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(RepostResponse.fromEntity(repost));
    }

    @GetMapping("/posts/{postId}/reposts")
    @Operation(summary = "리포스트 목록 조회", description = "특정 리포스트 목록 정보를 가져온다.")
    public ResponseEntity<List<RepostResponse>> getReposts(@PathVariable Long postId) {
        List<RepostResponse> repostResponses = repostService.getAllByPostId(postId).stream().map(RepostResponse::fromEntity).toList();
        return ResponseEntity.ok(repostResponses);
    }

    @GetMapping("/users/reposts")
    @Operation(summary = "사용자의 리포스트 목록 조회", description = "현재 로그인한 사용자가 리포스트한 목록을 조회한다.")
    public ResponseEntity<List<RepostResponse>> getAllRepostsByUserId() {
        User user = userService.getCurrentUser();
        List<RepostResponse> repostResponses = repostService.getAllByUser(user)
                .stream().map(RepostResponse::fromEntity).toList();
        return ResponseEntity.ok(repostResponses);
    }

    @DeleteMapping("/reposts/{repostId}")
    @Operation(summary = "리포스트 취소", description = "현재 로그인한 사용자의 특정 리포스트를 취소한다.")
    public ResponseEntity<Void> deleteRepost(@PathVariable Long repostId) {
        repostService.delete(repostId);
        return ResponseEntity.noContent().build();
    }
}