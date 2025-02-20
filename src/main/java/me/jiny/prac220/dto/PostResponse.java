package me.jiny.prac220.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jiny.prac220.domain.Post;
import me.jiny.prac220.user.dto.UserResponse;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PostResponse {
    private String title;
    private String content;
    private UserResponse author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostResponse fromEntity(Post post) {
        return PostResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .author(UserResponse.fromEntity(post.getAuthor()))
                .createdAt(post.getCreateAt())
                .updatedAt(post.getUpdateAt())
                .build();
    }
}