package me.jiny.prac220.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "hashtag", uniqueConstraints = {@UniqueConstraint(columnNames = "keyword")})
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String keyword;

    @ManyToMany(mappedBy = "hashtags")
    @JsonIgnore
    private Set<Post> posts = new HashSet<>();

    public Hashtag(String keyword) {
        this.keyword = keyword;
    }
}
