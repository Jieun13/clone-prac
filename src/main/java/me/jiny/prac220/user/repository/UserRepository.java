package me.jiny.prac220.user.repository;

import me.jiny.prac220.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    List<User> findByUsernameContaining(String username);
    List<User> findByNicknameContaining(String nickname);
}
