package com.odine.Freelance.Marketplace.API.job.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.odine.Freelance.Marketplace.API.job.dto.JobResponse;
import com.odine.Freelance.Marketplace.API.job.dto.JobCreateRequest;
import com.odine.Freelance.Marketplace.API.job.dto.JobUpdateRequest;
import com.odine.Freelance.Marketplace.API.job.entity.JobStatus;
import com.odine.Freelance.Marketplace.API.job.service.JobService;
import java.time.LocalDateTime;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@WebMvcTest(JobController.class)
@SuppressWarnings("null")
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobService jobService;

    @Test
    void createJob_returnsCreated() throws Exception {
        JobResponse response = new JobResponse();
        response.setId(10L);
        response.setFreelancerId(1L);
        response.setCreatedDate(LocalDateTime.of(2026, 4, 2, 10, 0));
        response.setStatus(JobStatus.IN_PROGRESS);
        response.setDescription("Build REST API");

        when(jobService.createJob(isA(JobCreateRequest.class))).thenReturn(response);

        String payload = """
                {
                  "freelancerId": 1,
                  "status": "IN_PROGRESS",
                  "description": "Build REST API"
                }
                """;

        mockMvc.perform(post("/api/v1/jobs")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.freelancerId").value(1))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void updateJob_returnsOk() throws Exception {
        JobResponse response = new JobResponse();
        response.setId(10L);
        response.setFreelancerId(1L);
        response.setStatus(JobStatus.FINISHED);
        response.setDescription("Delivered");

        when(jobService.updateJob(eq(10L), isA(JobUpdateRequest.class))).thenReturn(response);

        String payload = """
                {
                  "status": "FINISHED",
                  "description": "Delivered"
                }
                """;

        mockMvc.perform(patch("/api/v1/jobs/10")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.status").value("FINISHED"))
                .andExpect(jsonPath("$.description").value("Delivered"));
    }

    @Test
    void updateJob_whenEmptyUpdate_returnsBadRequest() throws Exception {
        when(jobService.updateJob(eq(10L), isA(JobUpdateRequest.class))).thenThrow(
                new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "At least one field must be provided for update: status or non-blank description"));

        mockMvc.perform(patch("/api/v1/jobs/10")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/api/v1/jobs/10"));
    }
}
