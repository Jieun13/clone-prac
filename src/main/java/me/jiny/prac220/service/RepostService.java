package me.jiny.prac220.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.jiny.prac220.domain.Post;
import me.jiny.prac220.domain.Repost;
import me.jiny.prac220.repository.PostRepository;
import me.jiny.prac220.repository.RepostRepository;
import me.jiny.prac220.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepostService {

    private final RepostRepository repostRepository;
    private final PostRepository postRepository;

    @Transactional
    public Repost create(User user, Long postId){
        Post originalPost = postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("Post not found"));
        Repost repost = Repost.builder()
                .originalPost(originalPost)
                .user(user)
                .build();
        return repostRepository.save(repost);
    }

    public Repost getRepostByPostIdAndUser(Long postId, User user){
        return repostRepository.findRepostByOriginalPostIdAndUser(postId, user).orElse(null);
    }

    public List<Repost> getAllByUser(User user){
        return repostRepository.findAllByUser(user);
    }

    public List<Repost> getAllByPostId(Long postId){
        return repostRepository.findAllByOriginalPostId(postId);
    }

    public Repost getRepostById(Long id){
        return repostRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Repost not found"));
    }

    @Transactional
    public void delete(Long id){
        Repost repost = repostRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Repost not found"));
        repostRepository.delete(repost);
    }
}