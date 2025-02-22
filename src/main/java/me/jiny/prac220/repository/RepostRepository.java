package me.jiny.prac220.repository;

import me.jiny.prac220.domain.Repost;
import me.jiny.prac220.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepostRepository extends JpaRepository<Repost, Long> {
    List<Repost> findAllByUser(User user);
}
