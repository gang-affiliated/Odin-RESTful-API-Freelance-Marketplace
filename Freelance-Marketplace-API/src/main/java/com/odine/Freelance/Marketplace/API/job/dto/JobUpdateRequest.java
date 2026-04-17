package com.odine.Freelance.Marketplace.API.job.dto;

import com.odine.Freelance.Marketplace.API.job.entity.JobStatus;

public class JobUpdateRequest {
    private JobStatus status;
    private String description;

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
