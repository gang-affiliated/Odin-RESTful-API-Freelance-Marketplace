package com.odine.Freelance.Marketplace.API.freelancer.controller;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.odine.Freelance.Marketplace.API.freelancer.dto.FreelancerResponse;
import com.odine.Freelance.Marketplace.API.freelancer.entity.EvaluationStatus;
import com.odine.Freelance.Marketplace.API.freelancer.entity.FreelancerType;
import com.odine.Freelance.Marketplace.API.freelancer.service.FreelancerService;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.server.ResponseStatusException;

@WebMvcTest(FreelancerController.class)
@SuppressWarnings("null")
class FreelancerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FreelancerService freelancerService;

    @Test
    void createFreelancer_returnsCreated() throws Exception {
        FreelancerResponse response = new FreelancerResponse();
        response.setId(1L);
        response.setName("Alice");
        response.setEmail("alice@example.com");
        response.setFreelancerType(FreelancerType.DESIGNER);
        response.setEvaluationStatus(EvaluationStatus.PENDING);

        when(freelancerService.createFreelancer(isA(com.odine.Freelance.Marketplace.API.freelancer.dto.FreelancerCreateRequest.class)))
                .thenReturn(response);

        String payload = """
                {
                  "name": "Alice",
                  "email": "alice@example.com",
                  "phone": "555-0001",
                  "city": "Istanbul",
                  "freelancerType": "DESIGNER",
                  "portfolioUrl": "https://portfolio.example/alice",
                  "designTools": ["Figma"]
                }
                """;

        mockMvc.perform(post("/api/v1/freelancers")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.evaluationStatus").value("PENDING"));
    }

    @Test
    void createFreelancer_whenInvalidPayload_returnsBadRequest() throws Exception {
        String invalidPayload = """
                {
                  "name": "",
                  "email": "not-an-email",
                  "phone": "",
                  "city": "",
                  "freelancerType": null
                }
                """;

        mockMvc.perform(post("/api/v1/freelancers")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.path").value("/api/v1/freelancers"));
    }

    @Test
    void createFreelancer_whenDuplicateEmail_returnsConflict() throws Exception {
        when(freelancerService.createFreelancer(isA(com.odine.Freelance.Marketplace.API.freelancer.dto.FreelancerCreateRequest.class)))
                .thenThrow(
                new ResponseStatusException(HttpStatus.CONFLICT, "Freelancer email already exists"));

        String payload = """
                {
                  "name": "Alice",
                  "email": "alice@example.com",
                  "phone": "555-0001",
                  "city": "Istanbul",
                  "freelancerType": "DESIGNER",
                  "portfolioUrl": "https://portfolio.example/alice",
                  "designTools": ["Figma"]
                }
                """;

        mockMvc.perform(post("/api/v1/freelancers")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(Objects.requireNonNull(payload)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Freelancer email already exists"))
                .andExpect(jsonPath("$.path").value("/api/v1/freelancers"));
    }
}
