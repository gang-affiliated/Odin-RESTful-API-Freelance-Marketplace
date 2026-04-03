package com.odine.Freelance.Marketplace.API.freelancer.repository;

import com.odine.Freelance.Marketplace.API.freelancer.entity.Freelancer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {
    Optional<Freelancer> findByEmail(String email);
}
