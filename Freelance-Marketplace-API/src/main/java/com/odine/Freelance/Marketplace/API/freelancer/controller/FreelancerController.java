package com.odine.Freelance.Marketplace.API.freelancer.controller;

import com.odine.Freelance.Marketplace.API.freelancer.dto.FreelancerCreateRequest;
import com.odine.Freelance.Marketplace.API.freelancer.dto.FreelancerResponse;
import com.odine.Freelance.Marketplace.API.freelancer.entity.FreelancerType;
import com.odine.Freelance.Marketplace.API.freelancer.service.FreelancerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/freelancers")
@Tag(name = "Freelancers", description = "Freelancer management endpoints")
public class FreelancerController {

    private final FreelancerService freelancerService;

    public FreelancerController(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    @PostMapping
    @Operation(summary = "Create freelancer", description = "Creates a freelancer and triggers asynchronous evaluation.")
    public ResponseEntity<FreelancerResponse> createFreelancer(
            @Valid @RequestBody @NonNull FreelancerCreateRequest request) {
        FreelancerResponse response = freelancerService.createFreelancer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{freelancerId}")
    @Operation(summary = "Get freelancer by id")
    public ResponseEntity<FreelancerResponse> getFreelancer(@PathVariable @NonNull Long freelancerId) {
        FreelancerResponse response = freelancerService.getFreelancer(freelancerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List all freelancers")
    public ResponseEntity<List<FreelancerResponse>> listFreelancers() {
        return ResponseEntity.ok(freelancerService.listFreelancers());
    }

    @GetMapping("/search")
    @Operation(summary = "Search freelancers with optional filters")
    public ResponseEntity<List<FreelancerResponse>> searchFreelancers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) FreelancerType type,
            @RequestParam(required = false) String tool,
            @RequestParam(required = false) String specialty) {
        return ResponseEntity.ok(
                freelancerService.searchFreelancers(name, city, type, tool, specialty));
    }
}
