package com.odine.Freelance.Marketplace.API.comment.repository;

import com.odine.Freelance.Marketplace.API.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByJobId(Long jobId);
}
