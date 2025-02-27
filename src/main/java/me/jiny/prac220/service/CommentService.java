package me.jiny.prac220.service;

import lombok.RequiredArgsConstructor;
import me.jiny.prac220.domain.Comment;
import me.jiny.prac220.domain.Post;
import me.jiny.prac220.dto.CommentRequest;
import me.jiny.prac220.repository.CommentRepository;
import me.jiny.prac220.user.domain.User;
import me.jiny.prac220.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;

    public Comment create(CommentRequest commentRequest, Post post) {
        User user = userService.getCurrentUser();
        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .parent(null)
                .post(post)
                .author(user)
                .build();
        return commentRepository.save(comment);
    }

    public Comment createCtoComment(CommentRequest commentRequest, Comment parent) {
        User user = userService.getCurrentUser();
        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .parent(parent)
                .post(null)
                .author(user)
                .build();
        parent.addChild(comment);
        return commentRepository.save(comment);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<Comment> findByPost(Post post) {
        return commentRepository.findCommentsByPost(post);
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}