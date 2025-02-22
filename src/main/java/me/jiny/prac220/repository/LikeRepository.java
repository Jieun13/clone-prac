package me.jiny.prac220.repository;

import me.jiny.prac220.domain.Like;
import me.jiny.prac220.domain.Post;
import me.jiny.prac220.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findAllByPostId(Long postId);

    List<Like> findAllByUserId(Long userId);
}