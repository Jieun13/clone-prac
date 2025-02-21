package me.jiny.prac220.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jiny.prac220.domain.Like;
import me.jiny.prac220.user.dto.UserResponse;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponse {
    private Long id;
    private UserResponse user;
    private PostResponse post;

    public static LikeResponse fromEntity(Like like) {
        return LikeResponse.builder()
                .id(like.getId())
                .user(UserResponse.fromEntity(like.getUser()))
                .post(PostResponse.fromEntity(like.getPost()))
                .build();
    }
}