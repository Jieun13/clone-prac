package me.jiny.prac220.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import me.jiny.prac220.domain.Post;
import me.jiny.prac220.domain.Follow;
import me.jiny.prac220.dto.PostRequest;
import me.jiny.prac220.repository.FollowRepository;
import me.jiny.prac220.repository.PostRepository;
import me.jiny.prac220.user.domain.User;
import me.jiny.prac220.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowService followService;
    private final FollowRepository followRepository;

    @Transactional
    public Post create(PostRequest request){
        String authorEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userRepository.findByEmail(authorEmail).orElseThrow(EntityNotFoundException::new);
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .build();
        return postRepository.save(post);
    }

    public Post findById(Long id){
        return postRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Post> findAll() {
        return postRepository.findAll(Sort.by("createdAt").descending());
    }

    public List<Post> findByAuthor(User author){
        return postRepository.findByAuthorOrderByCreatedAtDesc(author);
    }

    public List<Post> getUserFeed(Long userId){
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        List<User> followingUsers = user.getFollowing()
                .stream().map(Follow::getFollowing).toList();
        return postRepository.findByAuthorInOrderByCreatedAtDesc(followingUsers);
    }

    @Transactional
    public Post update(PostRequest request, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));
        post.update(request.getTitle(), request.getContent());
        return postRepository.save(post);
    }

    @Transactional
    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }
}
