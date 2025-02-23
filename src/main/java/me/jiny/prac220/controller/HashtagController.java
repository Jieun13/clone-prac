package me.jiny.prac220.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.jiny.prac220.domain.Hashtag;
import me.jiny.prac220.service.HashtagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Hashtag", description = "해시태그 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hashtag")
public class HashtagController {
    private final HashtagService hashtagService;

    @Operation(summary = "해시태그 생성", description = "새로운 해시태그를 추가합니다.")
    @PostMapping
    public ResponseEntity<Hashtag> create(
            @Parameter(description = "추가할 해시태그 키워드", example = "SpringBoot")
            @RequestParam String keyword) {
        Hashtag hashtag = hashtagService.create(keyword);
        return ResponseEntity.ok(hashtag);
    }

    @Operation(summary = "모든 해시태그 조회", description = "저장된 모든 해시태그를 반환합니다.")
    @GetMapping
    public ResponseEntity<List<Hashtag>> getAll() {
        List<Hashtag> hashtags = hashtagService.findAll();
        return ResponseEntity.ok(hashtags);
    }

    @Operation(summary = "해시태그 조회", description = "카테고리 키워드로 해시태그를 검색합니다.")
    @GetMapping("/{categoryKeyword}")
    public ResponseEntity<Hashtag> getByCategoryKeyword(
            @Parameter(description = "조회할 해시태그 키워드", example = "Java")
            @PathVariable String categoryKeyword) {
        Hashtag hashtag = hashtagService.findByKeyword(categoryKeyword);
        return ResponseEntity.ok(hashtag);
    }
}