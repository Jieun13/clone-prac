package me.jiny.prac220.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jiny.prac220.user.domain.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Repost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Post originalPost;

    private LocalDateTime createdAt;

    @Builder
    public Repost(User user, Post originalPost) {
        this.user = user;
        this.originalPost = originalPost;
        this.createdAt = LocalDateTime.now();
    }
}