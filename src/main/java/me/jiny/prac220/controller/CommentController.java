package me.jiny.prac220.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import me.jiny.prac220.domain.Comment;
import me.jiny.prac220.domain.Post;
import me.jiny.prac220.dto.CommentRequest;
import me.jiny.prac220.dto.CommentResponse;
import me.jiny.prac220.service.CommentService;
import me.jiny.prac220.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    @PostMapping("/post/{postId}/comments")
    @Operation(summary = "댓글 생성", description = "특정 게시글에 새로운 댓글을 생성합니다.")
    public ResponseEntity<CommentResponse> create(@RequestBody CommentRequest commentRequest, @PathVariable Long postId) {
        Post post = postService.findById(postId);
        Comment comment = commentService.create(commentRequest, post);
        return ResponseEntity.ok(CommentResponse.fromEntity(comment));
    }

    @PostMapping("/comments/{commentId}/replies")
    @Operation(summary = "대댓글 생성", description = "특정 댓글에 대한 대댓글을 생성합니다.")
    public ResponseEntity<CommentResponse> createCtoC(@RequestBody CommentRequest commentRequest, @PathVariable("commentId") Long commentId) {
        Comment parent = commentService.findById(commentId);
        Comment comment = commentService.createCtoComment(commentRequest, parent);
        return ResponseEntity.ok(CommentResponse.fromEntity(comment));
    }

    @GetMapping("/post/{postId}/comments")
    @Operation(summary = "댓글 조회", description = "특정 게시글의 모든 댓글을 조회합니다.")
    public ResponseEntity<List<CommentResponse>> getComments (@PathVariable Long postId) {
        Post post = postService.findById(postId);
        List<Comment> comments = commentService.findByPost(post);
        List<CommentResponse> commentResponses = comments.stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentResponses);
    }

    @DeleteMapping("/comments/{commentId}")
    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다.")
    public ResponseEntity<Void> delete(@PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }
}