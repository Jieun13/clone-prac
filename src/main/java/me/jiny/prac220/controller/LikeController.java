package me.jiny.prac220.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import me.jiny.prac220.domain.Like;
import me.jiny.prac220.dto.LikeResponse;
import me.jiny.prac220.service.LikeService;
import me.jiny.prac220.user.domain.User;
import me.jiny.prac220.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final UserService userService;

    @PostMapping("/posts/{postId}/likes")
    @Operation(summary = "포스트 좋아요", description = "특정 포스트에 좋아요를 추가한다.")
    public ResponseEntity<LikeResponse> create(@PathVariable Long postId) {
        User user = userService.getCurrentUser();
        Like like = likeService.save(postId, user);
        return ResponseEntity.ok(LikeResponse.fromEntity(like));
    }

    @GetMapping("/posts/{postId}/likes")
    @Operation(summary = "포스트 좋아요 전체 조회", description = "해당 포스트에 좋아요를 누른 사용자 목록을 조회한다.")
    public ResponseEntity<List<LikeResponse>> getAll(@PathVariable Long postId) {
        List<LikeResponse> likeResponses = likeService.findAllByPostId(postId).stream()
                .map(LikeResponse::fromEntity).toList();
        return ResponseEntity.ok(likeResponses);
    }

    @GetMapping("/users/likes")
    @Operation(summary = "사용자가 누른 좋아요 조회", description = "현재 로그인한 사용자가 좋아요한 게시글 목록을 조회한다.")
    public ResponseEntity<List<LikeResponse>> getAllLike() {
        User user = userService.getCurrentUser();
        List<LikeResponse> likeResponses = likeService.findAllByUserId(user.getId()).stream()
                .map(LikeResponse::fromEntity).toList();
        return ResponseEntity.ok(likeResponses);
    }

    @DeleteMapping("/likes/{likeId}")
    @Operation(summary = "좋아요 취소", description = "현재 사용자가 누른 특정 좋아요를 취소한다.")
    public ResponseEntity<Void> delete(@PathVariable Long likeId) {
        User user = userService.getCurrentUser();
        likeService.delete(likeId, user);
        return ResponseEntity.noContent().build();
    }
}