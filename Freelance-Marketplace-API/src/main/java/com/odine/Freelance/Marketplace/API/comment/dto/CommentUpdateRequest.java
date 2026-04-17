package com.odine.Freelance.Marketplace.API.comment.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentUpdateRequest {
    @NotBlank
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
