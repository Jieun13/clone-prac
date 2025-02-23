package me.jiny.prac220.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import me.jiny.prac220.domain.Hashtag;
import me.jiny.prac220.domain.Post;
import me.jiny.prac220.domain.Follow;
import me.jiny.prac220.dto.PostRequest;
import me.jiny.prac220.repository.FollowRepository;
import me.jiny.prac220.repository.HashtagRepository;
import me.jiny.prac220.repository.PostRepository;
import me.jiny.prac220.user.domain.User;
import me.jiny.prac220.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowService followService;
    private final FollowRepository followRepository;
    private final HashtagRepository hashtagRepository;
    private final HashtagService hashtagService;

    @Transactional
    public Post create(PostRequest request){

        // content에서 해시태그 자동 추출
        List<String> extractedHashtags = HashtagService.extractHashtags(request.getContent());
        Set<Hashtag> hashtagSet = extractedHashtags.stream()
                .map(keyword -> hashtagRepository.findByKeyword(keyword)
                        .orElseGet(() -> hashtagRepository.save(new Hashtag(keyword))))
                .collect(Collectors.toSet());

        String authorEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userRepository.findByEmail(authorEmail).orElseThrow(EntityNotFoundException::new);
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .build();
        post.addHashtags(hashtagSet);
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

    public List<Post> findByHashtag(String hashtag) {
        return postRepository.findByHashtagsKeyword(hashtag);
    }

    @Transactional
    public Post update(PostRequest request, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));
        post.update(request.getTitle(), request.getContent());

        // content에서 해시태그 자동 추출
        List<String> extractedHashtags = HashtagService.extractHashtags(request.getContent());
        Set<Hashtag> hashtagSet = extractedHashtags.stream()
                .map(keyword -> hashtagRepository.findByKeyword(keyword)
                        .orElseGet(() -> hashtagRepository.save(new Hashtag(keyword))))
                .collect(Collectors.toSet());

        post.updateHashtags(hashtagSet);
        return postRepository.save(post);
    }

    @Transactional
    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }
}
