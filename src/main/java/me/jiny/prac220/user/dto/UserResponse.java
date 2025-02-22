package me.jiny.prac220.user.dto;

import me.jiny.prac220.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String nickname;

    @Builder
    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .user(user)
                .build();
    }
}