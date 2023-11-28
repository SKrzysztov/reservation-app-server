package com.example.ReservationAppBackEnd.comment.service;


import com.example.ReservationAppBackEnd.comment.domain.Comment;
import com.example.ReservationAppBackEnd.comment.api.CommentRequest;
import com.example.ReservationAppBackEnd.comment.repository.CommentRepository;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.repository.CustomServiceProviderRepository;
import com.example.ReservationAppBackEnd.customServiceProvider.service.CustomServiceProviderService;
import com.example.ReservationAppBackEnd.error.NotFoundException;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.example.ReservationAppBackEnd.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final CustomServiceProviderService customServiceProviderService;
    private final CustomServiceProviderRepository serviceProviderRepository;

    public Comment createComment(CommentRequest commentRequest) {
        User user = userService.getLoggedUser();
        CustomServiceProvider serviceProvider = getExistingServiceProvider(commentRequest.serviceProviderId());

        // Stwórz nowy komentarz
        Comment comment = Comment.builder()
                .user(user)
                .serviceProvider(serviceProvider)
                .content(commentRequest.content())
                .opinion(commentRequest.opinion())
                .build();

        // Zapisz komentarz
        Comment savedComment = commentRepository.save(comment);
        serviceProvider.getComments().add(savedComment);
        serviceProvider.recalculateAverageOpinion();
        customServiceProviderService.saveServiceProvider(serviceProvider);
        return savedComment;

    }
    private CustomServiceProvider getExistingServiceProvider(Long serviceProviderId) {
        Optional<CustomServiceProvider> serviceProvider = serviceProviderRepository.findById(serviceProviderId);
        return serviceProvider.orElseThrow(() -> new NotFoundException("Service provider not found with id: " + serviceProviderId));
    }

}
