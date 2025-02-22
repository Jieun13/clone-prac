package me.jiny.prac220.dto;

import lombok.Builder;
import lombok.Getter;
import me.jiny.prac220.domain.Follow;
import me.jiny.prac220.user.dto.UserResponse;

@Getter
@Builder
public class FollowResponse {
    private Long id;
    private UserResponse follower;
    private UserResponse following;

    public static FollowResponse fromEntity(Follow follow) {
        return FollowResponse.builder()
                .id(follow.getId())
                .follower(UserResponse.fromEntity(follow.getFollower()))
                .following(UserResponse.fromEntity(follow.getFollowing()))
                .build();
    }
}