package me.jiny.prac220.service;

import lombok.RequiredArgsConstructor;
import me.jiny.prac220.domain.Follow;
import me.jiny.prac220.repository.FollowRepository;
import me.jiny.prac220.user.domain.User;
import me.jiny.prac220.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public Follow create(Long userId, Long followingId){
        if(followRepository.existsByFollowerIdAndFollowingId(userId, followingId)){
            throw new IllegalArgumentException("이미 팔로우한 유저입니다.");
        }
        User follower = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("User not found."));
        User following = userRepository.findById(followingId).orElseThrow(()->new IllegalArgumentException("User not found."));

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        return followRepository.save(follow);
    }

    public List<Follow> findAllFollowingsByUserId(Long userId){ // userId가 팔로우 하는 사람 목록 조회
        return followRepository.findAllByFollowerId(userId);
    }

    public List<Follow> findAllFollowersByUserId(Long userId){ // userId를 팔로우한 사람 목록 조회
        return followRepository.findAllByFollowingId(userId);
    }

    public void delete(Long userId, Long followingId){
        followRepository.deleteByFollowerIdAndFollowingId(userId, followingId);
    }
}
