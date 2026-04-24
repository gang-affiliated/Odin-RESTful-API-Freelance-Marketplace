package com.odine.Freelance.Marketplace.API.freelancer.service;

import com.odine.Freelance.Marketplace.API.freelancer.entity.EvaluationStatus;
import com.odine.Freelance.Marketplace.API.freelancer.entity.Freelancer;
import com.odine.Freelance.Marketplace.API.freelancer.entity.FreelancerType;
import com.odine.Freelance.Marketplace.API.freelancer.repository.FreelancerRepository;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FreelancerEvaluationService {

    private final FreelancerRepository freelancerRepository;
    private final long mockDelayMs;

    public FreelancerEvaluationService(
            FreelancerRepository freelancerRepository,
            @Value("${freelancer.evaluation.mock-delay-ms:60000}") long mockDelayMs) {
        this.freelancerRepository = freelancerRepository;
        this.mockDelayMs = mockDelayMs;
    }

    public void evaluateFreelancer(Long freelancerId) {
        Long safeFreelancerId = Objects.requireNonNull(freelancerId, "freelancerId cannot be null");
        applyMockDelay();

        try {
            Freelancer freelancer = freelancerRepository.findById(safeFreelancerId).orElse(null);
            if (freelancer == null) {
                return;
            }

            int rawScore = calculateRawScore(freelancer);
            freelancer.setEvaluationScore(normalizeToOneTen(rawScore));
            freelancer.setEvaluationStatus(EvaluationStatus.COMPLETED);
            freelancerRepository.save(freelancer);
        } catch (Exception ignored) {
            freelancerRepository.findById(safeFreelancerId).ifPresent(freelancer -> {
                freelancer.setEvaluationStatus(EvaluationStatus.FAILED);
                freelancer.setEvaluationScore(null);
                freelancerRepository.save(freelancer);
            });
        }
    }

    private void applyMockDelay() {
        if (mockDelayMs <= 0) {
            return;
        }
        try {
            Thread.sleep(mockDelayMs);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Evaluation delay interrupted", ex);
        }
    }

    private int calculateRawScore(Freelancer freelancer) {
        if (freelancer.getFreelancerType() == FreelancerType.DESIGNER) {
            return safeSize(freelancer.getDesignTools());
        }
        return safeSize(freelancer.getSpecialties()) * safeSize(freelancer.getSoftwareLanguages());
    }

    private int normalizeToOneTen(int score) {
        return Math.max(1, Math.min(10, score));
    }

    private int safeSize(Set<String> values) {
        return values == null ? 0 : values.size();
    }
}
