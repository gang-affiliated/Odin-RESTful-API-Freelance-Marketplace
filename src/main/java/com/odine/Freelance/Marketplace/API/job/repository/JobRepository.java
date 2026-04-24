package com.odine.Freelance.Marketplace.API.job.repository;

import com.odine.Freelance.Marketplace.API.job.entity.Job;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByFreelancer_Id(Long freelancerId);
}
