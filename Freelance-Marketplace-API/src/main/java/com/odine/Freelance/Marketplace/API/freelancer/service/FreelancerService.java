package com.odine.Freelance.Marketplace.API.freelancer.service;

import com.odine.Freelance.Marketplace.API.freelancer.entity.EvaluationStatus;
import com.odine.Freelance.Marketplace.API.freelancer.entity.Freelancer;
import com.odine.Freelance.Marketplace.API.freelancer.entity.FreelancerType;
import com.odine.Freelance.Marketplace.API.freelancer.dto.FreelancerCreateRequest;
import com.odine.Freelance.Marketplace.API.freelancer.dto.FreelancerResponse;
import com.odine.Freelance.Marketplace.API.freelancer.repository.FreelancerRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FreelancerService {

    private final FreelancerRepository freelancerRepository;
    private final FreelancerEvaluationService freelancerEvaluationService;

    public FreelancerService(
            FreelancerRepository freelancerRepository,
            FreelancerEvaluationService freelancerEvaluationService) {
        this.freelancerRepository = freelancerRepository;
        this.freelancerEvaluationService = freelancerEvaluationService;
    }

    public FreelancerResponse createFreelancer(@NonNull FreelancerCreateRequest request) {
        validateTypeSpecificFields(request);

        freelancerRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Freelancer email already exists");
        });

        Freelancer freelancer = mapCreateRequestToEntity(request);

        Freelancer savedFreelancer = Objects.requireNonNull(
                freelancerRepository.save(freelancer),
                "Saved freelancer must not be null");
        freelancerEvaluationService.evaluateAsync(savedFreelancer.getId());
        return mapEntityToResponse(savedFreelancer);
    }

    @Transactional(readOnly = true)
    public FreelancerResponse getFreelancer(@NonNull Long freelancerId) {
        Long safeFreelancerId = Objects.requireNonNull(freelancerId, "freelancerId cannot be null");
        Freelancer freelancer = freelancerRepository.findById(safeFreelancerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Freelancer not found"));
        return mapEntityToResponse(Objects.requireNonNull(freelancer, "Freelancer must not be null"));
    }

    @Transactional(readOnly = true)
    public List<FreelancerResponse> listFreelancers() {
        return freelancerRepository.findAll()
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FreelancerResponse> searchFreelancers(
            @Nullable String name,
            @Nullable String city,
            @Nullable FreelancerType type,
            @Nullable String tool,
            @Nullable String specialty) {
        return freelancerRepository.findAll()
                .stream()
                .filter(freelancer -> matchesContains(freelancer.getName(), name))
                .filter(freelancer -> matchesContains(freelancer.getCity(), city))
                .filter(freelancer -> type == null || freelancer.getFreelancerType() == type)
                .filter(freelancer -> matchesCollectionValue(freelancer.getDesignTools(), tool))
                .filter(freelancer -> matchesCollectionValue(freelancer.getSpecialties(), specialty))
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    private void validateTypeSpecificFields(@NonNull FreelancerCreateRequest request) {
        if (request.getFreelancerType() == FreelancerType.DESIGNER) {
            if (isBlank(request.getPortfolioUrl()) || isNullOrEmpty(request.getDesignTools())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Designer requires portfolioUrl and at least one design tool");
            }
        } else if (request.getFreelancerType() == FreelancerType.SOFTWARE_DEVELOPER) {
            if (isNullOrEmpty(request.getSoftwareLanguages()) || isNullOrEmpty(request.getSpecialties())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Software developer requires at least one language and one specialty");
            }
        }
    }

    @NonNull
    private Freelancer mapCreateRequestToEntity(@NonNull FreelancerCreateRequest request) {
        Freelancer freelancer = new Freelancer();
        freelancer.setId(null);
        freelancer.setName(request.getName());
        freelancer.setEmail(request.getEmail());
        freelancer.setPhone(request.getPhone());
        freelancer.setCity(request.getCity());
        freelancer.setFreelancerType(request.getFreelancerType());
        freelancer.setPortfolioUrl(request.getPortfolioUrl());
        freelancer.setDesignTools(orEmptySet(request.getDesignTools()));
        freelancer.setSoftwareLanguages(orEmptySet(request.getSoftwareLanguages()));
        freelancer.setSpecialties(orEmptySet(request.getSpecialties()));
        freelancer.setAdditionalFields(
                request.getAdditionalFields() == null ? new HashMap<>() : new HashMap<>(request.getAdditionalFields()));
        freelancer.setEvaluationStatus(EvaluationStatus.PENDING);
        freelancer.setEvaluationScore(null);
        return freelancer;
    }

    @NonNull
    private FreelancerResponse mapEntityToResponse(@NonNull Freelancer freelancer) {
        FreelancerResponse response = new FreelancerResponse();
        response.setId(freelancer.getId());
        response.setName(freelancer.getName());
        response.setEmail(freelancer.getEmail());
        response.setPhone(freelancer.getPhone());
        response.setCity(freelancer.getCity());
        response.setFreelancerType(freelancer.getFreelancerType());
        response.setPortfolioUrl(freelancer.getPortfolioUrl());
        response.setDesignTools(orEmptySet(freelancer.getDesignTools()));
        response.setSoftwareLanguages(orEmptySet(freelancer.getSoftwareLanguages()));
        response.setSpecialties(orEmptySet(freelancer.getSpecialties()));
        response.setEvaluationScore(freelancer.getEvaluationScore());
        response.setEvaluationStatus(freelancer.getEvaluationStatus());
        response.setAdditionalFields(freelancer.getAdditionalFields() == null
                ? new HashMap<>()
                : new HashMap<>(freelancer.getAdditionalFields()));
        return response;
    }

    @NonNull
    private Set<String> orEmptySet(Set<String> values) {
        return values == null ? new HashSet<>() : new HashSet<>(values);
    }

    private boolean isNullOrEmpty(Set<String> values) {
        return values == null || values.isEmpty();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private boolean matchesContains(String value, String query) {
        if (isBlank(query)) {
            return true;
        }
        if (value == null) {
            return false;
        }
        return value.toLowerCase().contains(query.toLowerCase());
    }

    private boolean matchesCollectionValue(Set<String> values, String query) {
        if (isBlank(query)) {
            return true;
        }
        if (values == null || values.isEmpty()) {
            return false;
        }
        return values.stream().anyMatch(value -> value != null && value.equalsIgnoreCase(query));
    }
}
