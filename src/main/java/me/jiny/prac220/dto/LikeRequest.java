package me.jiny.prac220.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeRequest {
    @NotNull
    private Long postId;

    public LikeRequest(Long postId) {
        this.postId = postId;
    }
}