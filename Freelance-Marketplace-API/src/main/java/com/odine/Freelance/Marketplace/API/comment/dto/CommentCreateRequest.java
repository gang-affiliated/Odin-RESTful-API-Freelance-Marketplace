package com.odine.Freelance.Marketplace.API.comment.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class CommentCreateRequest {
    private LocalDateTime createdDate;

    @NotBlank
    private String commenterName;

    @NotBlank
    private String comment;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
