package me.jiny.prac220.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import me.jiny.prac220.domain.Post;
import me.jiny.prac220.dto.PostRequest;
import me.jiny.prac220.dto.PostResponse;
import me.jiny.prac220.service.PostService;
import me.jiny.prac220.user.domain.User;
import me.jiny.prac220.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping("/posts")
    @Operation(summary = "포스트 생성", description = "새로운 포스트를 생성한다.")
    public ResponseEntity<PostResponse> create(@RequestBody PostRequest postRequest) {
        Post post = postService.create(postRequest);
        return ResponseEntity.ok(PostResponse.fromEntity(post));
    }

    @GetMapping("/posts")
    @Operation(summary = "전체 포스트 조회", description = "전체 포스트를 조회한다.")
    public ResponseEntity<List<PostResponse>> getAll() {
        List<Post> posts = postService.findAll();
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postResponses);
    }

    @GetMapping("/posts/feed")
    @Operation(summary = "나의 타임라인 조회", description = "팔로우한 유저와 나의 포스트만 조회")
    public ResponseEntity<List<PostResponse>> getFeed() {
        User currentUser = userService.getCurrentUser();
        List<Post> posts = Stream.concat(
                        postService.getUserFeed(currentUser.getId()).stream(),
                        postService.findByAuthor(currentUser).stream()
                )
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed()) // 내림차순 정렬
                .toList();
        return ResponseEntity.ok(posts.stream().map(PostResponse::fromEntity).toList());
    }

    @GetMapping("/posts/user/{authorId}")
    @Operation(summary = "사용자 아이디로 포스트 조회", description = "사용자가 작성한 포스트를 조회한다.")
    public ResponseEntity<List<PostResponse>> findAllByAuthor(@PathVariable Long authorId) {
        User author = userService.findById(authorId);
        List<Post> posts = postService.findByAuthor(author);
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postResponses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> findByHashtag(@RequestParam String hashtag) {
        List<Post> posts = postService.findByHashtag(hashtag);
        return ResponseEntity.ok(posts.stream().map(PostResponse::fromEntity).toList());
    }

    @GetMapping("/posts/{postId}")
    @Operation(summary = "포스트 아이디로 포스트 조회", description = "해당 포스트를 조회한다.")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        Post post = postService.findById(postId);
        return ResponseEntity.ok(PostResponse.fromEntity(post));
    }

    @PutMapping("/posts/{postId}")
    @Operation(summary = "포스트 아이디로 포스트 수정", description = "해당 포스트를 수정한다.")
    public ResponseEntity<PostResponse> update(@RequestBody PostRequest postRequest, @PathVariable Long postId) {
        Post post = postService.update(postRequest, postId);
        return ResponseEntity.ok(PostResponse.fromEntity(post));
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "포스트 아이디로 포스트 삭제", description = "해당 포스트를 삭제한다.")
    public ResponseEntity<PostResponse> delete(@PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }
}