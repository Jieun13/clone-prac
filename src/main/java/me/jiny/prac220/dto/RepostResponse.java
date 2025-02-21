package me.jiny.prac220.dto;

import lombok.Builder;
import lombok.Getter;
import me.jiny.prac220.domain.Repost;
import me.jiny.prac220.user.dto.UserResponse;

import java.time.LocalDateTime;

@Getter
@Builder
public class RepostResponse {
    private Long id;
    private UserResponse user;
    private PostResponse post;
    private LocalDateTime createdAt;

    public static RepostResponse fromEntity(Repost repost) {
        return RepostResponse.builder()
                .id(repost.getId())
                .user(UserResponse.fromEntity(repost.getUser()))
                .post(PostResponse.fromEntity(repost.getOriginalPost()))
                .build();
    }
}