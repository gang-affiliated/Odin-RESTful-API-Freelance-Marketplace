package com.odine.Freelance.Marketplace.API.comment.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.odine.Freelance.Marketplace.API.comment.dto.CommentResponse;
import com.odine.Freelance.Marketplace.API.comment.dto.CommentCreateRequest;
import com.odine.Freelance.Marketplace.API.comment.dto.CommentUpdateRequest;
import com.odine.Freelance.Marketplace.API.comment.service.CommentService;
import java.time.LocalDateTime;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentController.class)
@SuppressWarnings("null")
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    @Test
    void createComment_returnsCreated() throws Exception {
        CommentResponse response = new CommentResponse();
        response.setId(100L);
        response.setJobId(10L);
        response.setCommenterName("Manager");
        response.setComment("Looks good");
        response.setCreatedDate(LocalDateTime.of(2026, 4, 2, 11, 0));

        when(commentService.createComment(eq(10L), isA(CommentCreateRequest.class))).thenReturn(response);

        String payload = """
                {
                  "commenterName": "Manager",
                  "comment": "Looks good"
                }
                """;

        mockMvc.perform(post("/api/v1/jobs/10/comments")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.jobId").value(10))
                .andExpect(jsonPath("$.comment").value("Looks good"));
    }

    @Test
    void createComment_whenBlankFields_returnsBadRequest() throws Exception {
        String invalidPayload = """
                {
                  "commenterName": "",
                  "comment": ""
                }
                """;

        mockMvc.perform(post("/api/v1/jobs/10/comments")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.path").value("/api/v1/jobs/10/comments"));
    }

    @Test
    void updateComment_returnsOk() throws Exception {
        CommentResponse response = new CommentResponse();
        response.setId(100L);
        response.setJobId(10L);
        response.setCommenterName("Manager");
        response.setComment("Updated text");

        when(commentService.updateComment(eq(100L), isA(CommentUpdateRequest.class))).thenReturn(response);

        String payload = """
                {
                  "comment": "Updated text"
                }
                """;

        mockMvc.perform(patch("/api/v1/comments/100")
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.comment").value("Updated text"));
    }
}
