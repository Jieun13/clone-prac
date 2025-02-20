package me.jiny.prac220.repository;

import me.jiny.prac220.domain.Post;
import me.jiny.prac220.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByAuthor(User author);
}
