package com.example.ReservationAppBackEnd.comment.repository;

import com.example.ReservationAppBackEnd.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}