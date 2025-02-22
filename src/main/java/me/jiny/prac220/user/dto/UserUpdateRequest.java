package me.jiny.prac220.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserUpdateRequest {
    private String nickname;
}
