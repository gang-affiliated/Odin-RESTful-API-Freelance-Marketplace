package com.odine.Freelance.Marketplace.API.comment.controller;

import com.odine.Freelance.Marketplace.API.comment.dto.CommentCreateRequest;
import com.odine.Freelance.Marketplace.API.comment.dto.CommentResponse;
import com.odine.Freelance.Marketplace.API.comment.dto.CommentUpdateRequest;
import com.odine.Freelance.Marketplace.API.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Comments", description = "Comment endpoints for jobs")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/jobs/{jobId}/comments")
    @Operation(summary = "Create comment for job")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable @NonNull Long jobId,
            @Valid @RequestBody @NonNull CommentCreateRequest request) {
        CommentResponse response = commentService.createComment(jobId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/jobs/{jobId}/comments")
    @Operation(summary = "Get comments of job")
    public ResponseEntity<List<CommentResponse>> getCommentsOfJob(@PathVariable @NonNull Long jobId) {
        return ResponseEntity.ok(commentService.getCommentsOfJob(jobId));
    }

    @PatchMapping("/comments/{commentId}")
    @Operation(summary = "Update comment text")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable @NonNull Long commentId,
            @Valid @RequestBody @NonNull CommentUpdateRequest request) {
        return ResponseEntity.ok(commentService.updateComment(commentId, request));
    }
}
