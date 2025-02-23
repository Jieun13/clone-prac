package me.jiny.prac220.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Builder
@Getter
public class PostRequest {
    private String title;
    private String content;
}