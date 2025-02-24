package me.jiny.prac220.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.jiny.prac220.domain.Like;
import me.jiny.prac220.domain.Post;
import me.jiny.prac220.repository.LikeRepository;
import me.jiny.prac220.repository.PostRepository;
import me.jiny.prac220.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Transactional
    public Like save(Long postId, User user){
        Post post = postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("Post not found"));
        Like like = Like.builder()
                .post(post)
                .user(user)
                .build();
        return likeRepository.save(like);
    }

    public Like findByPostAndUser(Long postId, User user){
        return likeRepository.findLikeByPostIdAndUserId(postId, user.getId());
    }

    public List<Like> findAllByPostId(Long postId){
        return likeRepository.findAllByPostId(postId);
    }

    public List<Like> findAllByUserId(Long userId){
        return likeRepository.findAllByUserId(userId);
    }

    @Transactional
    public void delete(Long id, User user){
        Like like = likeRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Like not found"));
        likeRepository.delete(like);
    }
}