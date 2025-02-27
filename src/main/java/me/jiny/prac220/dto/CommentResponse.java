package me.jiny.prac220.dto;

import lombok.Builder;
import lombok.Getter;
import me.jiny.prac220.domain.Comment;
import me.jiny.prac220.user.dto.UserResponse;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private UserResponse author;
    private LocalDateTime createdAt;

    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(UserResponse.fromEntity(comment.getAuthor()))
                .createdAt(comment.getCreatedAt())
                .build();
    }
}