package me.jiny.prac220.repository;

import me.jiny.prac220.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    List<Follow> findAllByFollowerId(Long userId);
    List<Follow> findAllByFollowingId(Long userId);
}