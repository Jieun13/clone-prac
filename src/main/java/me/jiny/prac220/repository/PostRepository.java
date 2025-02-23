package me.jiny.prac220.repository;

import io.lettuce.core.dynamic.annotation.Param;
import me.jiny.prac220.domain.Post;
import me.jiny.prac220.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorOrderByCreatedAtDesc(User author);

    List<Post> findByAuthorInOrderByCreatedAtDesc(List<User> authors);

    @Query("SELECT p FROM Post p JOIN p.hashtags h WHERE h.keyword = :hashtag")
    List<Post> findByHashtagsKeyword(@Param("hashtag") String hashtag);
}