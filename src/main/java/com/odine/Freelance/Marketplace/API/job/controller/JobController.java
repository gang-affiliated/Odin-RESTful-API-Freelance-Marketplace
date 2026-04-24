package com.odine.Freelance.Marketplace.API.job.controller;

import com.odine.Freelance.Marketplace.API.job.dto.JobCreateRequest;
import com.odine.Freelance.Marketplace.API.job.dto.JobResponse;
import com.odine.Freelance.Marketplace.API.job.dto.JobUpdateRequest;
import com.odine.Freelance.Marketplace.API.job.service.JobService;
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
@Tag(name = "Jobs", description = "Job management endpoints")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @Operation(summary = "Create job")
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody @NonNull JobCreateRequest request) {
        JobResponse response = jobService.createJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/freelancers/{freelancerId}/jobs")
    @Operation(summary = "Get jobs of freelancer")
    public ResponseEntity<List<JobResponse>> getJobsOfFreelancer(@PathVariable @NonNull Long freelancerId) {
        return ResponseEntity.ok(jobService.getJobsOfFreelancer(freelancerId));
    }

    @GetMapping("/jobs/{jobId}")
    @Operation(summary = "Get job by id")
    public ResponseEntity<JobResponse> getJob(@PathVariable @NonNull Long jobId) {
        return ResponseEntity.ok(jobService.getJob(jobId));
    }

    @PatchMapping("/jobs/{jobId}")
    @Operation(summary = "Update job status and/or description")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable @NonNull Long jobId,
            @RequestBody @NonNull JobUpdateRequest request) {
        return ResponseEntity.ok(jobService.updateJob(jobId, request));
    }
}
