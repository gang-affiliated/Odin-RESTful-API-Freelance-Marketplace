package com.odine.Freelance.Marketplace.API.freelancer.service;

import com.odine.Freelance.Marketplace.API.freelancer.entity.EvaluationStatus;
import com.odine.Freelance.Marketplace.API.freelancer.entity.Freelancer;
import com.odine.Freelance.Marketplace.API.freelancer.entity.FreelancerType;
import com.odine.Freelance.Marketplace.API.freelancer.repository.FreelancerRepository;
import java.util.Objects;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FreelancerEvaluationService {

    private final FreelancerRepository freelancerRepository;

    public FreelancerEvaluationService(FreelancerRepository freelancerRepository) {
        this.freelancerRepository = freelancerRepository;
    }

    @Transactional
    public void evaluateFreelancer(Long freelancerId) {
        Long safeFreelancerId = Objects.requireNonNull(freelancerId, "freelancerId cannot be null");
        try {
            Freelancer freelancer = freelancerRepository.findById(safeFreelancerId).orElse(null);
            if (freelancer == null) {
                return;
            }

            int rawScore = calculateRawScore(freelancer);
            freelancer.setEvaluationScore(normalizeToOneTen(rawScore));
            freelancer.setEvaluationStatus(EvaluationStatus.COMPLETED);
        } catch (Exception ignored) {
            freelancerRepository.findById(safeFreelancerId).ifPresent(freelancer -> {
                freelancer.setEvaluationStatus(EvaluationStatus.FAILED);
                freelancer.setEvaluationScore(null);
            });
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
