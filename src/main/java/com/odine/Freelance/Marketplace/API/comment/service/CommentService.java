package com.odine.Freelance.Marketplace.API.comment.service;

import com.odine.Freelance.Marketplace.API.comment.dto.CommentCreateRequest;
import com.odine.Freelance.Marketplace.API.comment.dto.CommentResponse;
import com.odine.Freelance.Marketplace.API.comment.dto.CommentUpdateRequest;
import com.odine.Freelance.Marketplace.API.comment.entity.Comment;
import com.odine.Freelance.Marketplace.API.comment.repository.CommentRepository;
import com.odine.Freelance.Marketplace.API.job.entity.Job;
import com.odine.Freelance.Marketplace.API.job.repository.JobRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final JobRepository jobRepository;

    public CommentService(CommentRepository commentRepository, JobRepository jobRepository) {
        this.commentRepository = commentRepository;
        this.jobRepository = jobRepository;
    }

    @Transactional
    public CommentResponse createComment(@NonNull Long jobId, @NonNull CommentCreateRequest request) {
        Long safeJobId = Objects.requireNonNull(jobId, "jobId cannot be null");
        Job job = jobRepository.findById(safeJobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));

        Comment comment = new Comment();
        comment.setId(null);
        comment.setJob(job);
        comment.setCommenterName(request.getCommenterName());
        comment.setCreatedDate(request.getCreatedDate());
        comment.setComment(request.getComment());

        Comment savedComment = Objects.requireNonNull(commentRepository.save(comment), "Saved comment must not be null");
        return mapToResponse(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsOfJob(@NonNull Long jobId) {
        Long safeJobId = Objects.requireNonNull(jobId, "jobId cannot be null");
        if (!jobRepository.existsById(safeJobId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found");
        }
        return commentRepository.findByJobId(safeJobId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse updateComment(@NonNull Long commentId, @NonNull CommentUpdateRequest request) {
        Long safeCommentId = Objects.requireNonNull(commentId, "commentId cannot be null");
        Comment comment = commentRepository.findById(safeCommentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        comment.setComment(request.getComment());
        return mapToResponse(comment);
    }

    private CommentResponse mapToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setJobId(comment.getJob().getId());
        response.setCommenterName(comment.getCommenterName());
        response.setCreatedDate(comment.getCreatedDate());
        response.setComment(comment.getComment());
        return response;
    }
}
