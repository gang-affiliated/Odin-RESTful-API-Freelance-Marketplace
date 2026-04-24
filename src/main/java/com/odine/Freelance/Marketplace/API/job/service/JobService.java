package com.odine.Freelance.Marketplace.API.job.service;

import com.odine.Freelance.Marketplace.API.freelancer.entity.Freelancer;
import com.odine.Freelance.Marketplace.API.freelancer.repository.FreelancerRepository;
import com.odine.Freelance.Marketplace.API.job.dto.JobCreateRequest;
import com.odine.Freelance.Marketplace.API.job.dto.JobResponse;
import com.odine.Freelance.Marketplace.API.job.dto.JobUpdateRequest;
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
public class JobService {

    private final JobRepository jobRepository;
    private final FreelancerRepository freelancerRepository;

    public JobService(JobRepository jobRepository, FreelancerRepository freelancerRepository) {
        this.jobRepository = jobRepository;
        this.freelancerRepository = freelancerRepository;
    }

    @Transactional
    public JobResponse createJob(@NonNull JobCreateRequest request) {
        Long freelancerId = Objects.requireNonNull(request.getFreelancerId(), "freelancerId cannot be null");
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Freelancer not found"));

        Job job = new Job();
        job.setId(null);
        job.setFreelancer(freelancer);
        job.setCreatedDate(request.getCreatedDate());
        job.setStatus(request.getStatus());
        job.setDescription(request.getDescription());

        Job savedJob = Objects.requireNonNull(jobRepository.save(job), "Saved job must not be null");
        return mapToResponse(savedJob);
    }

    @Transactional(readOnly = true)
    public List<JobResponse> getJobsOfFreelancer(@NonNull Long freelancerId) {
        Long safeFreelancerId = Objects.requireNonNull(freelancerId, "freelancerId cannot be null");
        if (!freelancerRepository.existsById(safeFreelancerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Freelancer not found");
        }

        return jobRepository.findByFreelancer_Id(safeFreelancerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public JobResponse getJob(@NonNull Long jobId) {
        Long safeJobId = Objects.requireNonNull(jobId, "jobId cannot be null");
        Job job = jobRepository.findById(safeJobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
        return mapToResponse(job);
    }

    @Transactional
    public JobResponse updateJob(@NonNull Long jobId, @NonNull JobUpdateRequest request) {
        Long safeJobId = Objects.requireNonNull(jobId, "jobId cannot be null");
        Job job = jobRepository.findById(safeJobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));

        boolean hasStatusUpdate = request.getStatus() != null;
        boolean hasDescriptionUpdate = request.getDescription() != null && !request.getDescription().isBlank();
        if (!hasStatusUpdate && !hasDescriptionUpdate) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "At least one field must be provided for update: status or non-blank description");
        }

        if (request.getStatus() != null) {
            job.setStatus(request.getStatus());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            job.setDescription(request.getDescription());
        }

        return mapToResponse(job);
    }

    private JobResponse mapToResponse(Job job) {
        JobResponse response = new JobResponse();
        response.setId(job.getId());
        response.setFreelancerId(job.getFreelancer().getId());
        response.setCreatedDate(job.getCreatedDate());
        response.setStatus(job.getStatus());
        response.setDescription(job.getDescription());
        return response;
    }
}
