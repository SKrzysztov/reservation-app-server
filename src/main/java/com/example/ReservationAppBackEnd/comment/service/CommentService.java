package com.example.ReservationAppBackEnd.comment.service;


import com.example.ReservationAppBackEnd.comment.domain.Comment;
import com.example.ReservationAppBackEnd.comment.api.CommentRequest;
import com.example.ReservationAppBackEnd.comment.repository.CommentRepository;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.repository.CustomServiceProviderRepository;
import com.example.ReservationAppBackEnd.customServiceProvider.service.CustomServiceProviderService;
import com.example.ReservationAppBackEnd.error.NotFoundException;
import com.example.ReservationAppBackEnd.error.UnauthorizedException;
import com.example.ReservationAppBackEnd.user.domain.Role;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.example.ReservationAppBackEnd.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final CustomServiceProviderService customServiceProviderService;
    private final CustomServiceProviderRepository serviceProviderRepository;

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
    public Comment createComment(@Valid CommentRequest commentRequest) {
        if (!userService.isUserLoggedIn()) {
            throw new UnauthorizedException("You are not logged in");
        } else {
            User user = userService.getLoggedUser();
            CustomServiceProvider serviceProvider = customServiceProviderService.getServiceProvider(commentRequest.serviceProviderId());
            Comment comment = Comment.builder()
                    .user(user)
                    .serviceProvider(serviceProvider)
                    .addedTime(LocalDateTime.now())
                    .content(commentRequest.content())
                    .opinion(commentRequest.opinion())
                    .build();
            Comment savedComment = commentRepository.save(comment);
            serviceProvider.recalculateAverageOpinion();
            customServiceProviderService.saveServiceProvider(serviceProvider);
            return savedComment;
        }
    }
    public Comment updateComment(Long commentId, @Valid CommentRequest updatedCommentRequest) {
        Comment existingComment = getCommentById(commentId);
        User loggedInUser = userService.getLoggedUser();
        if (!loggedInUser.equals(existingComment.getUser())) {
            throw new UnauthorizedException("You are not the owner of this comment");
        }
        CustomServiceProvider serviceProvider = customServiceProviderService.getServiceProvider(updatedCommentRequest.serviceProviderId());
        existingComment.setContent(updatedCommentRequest.content());
        existingComment.setOpinion(updatedCommentRequest.opinion());
        commentRepository.save(existingComment);
        customServiceProviderService.saveServiceProvider(serviceProvider);
        return existingComment;
    }

    public void deleteComment(Long commentId) {
        Comment existingComment = getCommentById(commentId);
        User loggedInUser = userService.getLoggedUser();

        if (!loggedInUser.getRole().equals(Role.ADMIN) && !loggedInUser.equals(existingComment.getUser())) {
            throw new UnauthorizedException("You are not allowed to delete this comment");
        }
        commentRepository.delete(existingComment);
    }
}
