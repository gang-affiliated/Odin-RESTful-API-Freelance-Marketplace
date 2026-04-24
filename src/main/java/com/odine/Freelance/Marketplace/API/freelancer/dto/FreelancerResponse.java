package com.odine.Freelance.Marketplace.API.freelancer.dto;

import com.odine.Freelance.Marketplace.API.freelancer.entity.EvaluationStatus;
import com.odine.Freelance.Marketplace.API.freelancer.entity.FreelancerType;
import java.util.Map;
import java.util.Set;

public class FreelancerResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String city;
    private FreelancerType freelancerType;
    private String portfolioUrl;
    private Set<String> designTools;
    private Set<String> softwareLanguages;
    private Set<String> specialties;
    private Integer evaluationScore;
    private EvaluationStatus evaluationStatus;
    private Map<String, String> additionalFields;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public FreelancerType getFreelancerType() {
        return freelancerType;
    }

    public void setFreelancerType(FreelancerType freelancerType) {
        this.freelancerType = freelancerType;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public Set<String> getDesignTools() {
        return designTools;
    }

    public void setDesignTools(Set<String> designTools) {
        this.designTools = designTools;
    }

    public Set<String> getSoftwareLanguages() {
        return softwareLanguages;
    }

    public void setSoftwareLanguages(Set<String> softwareLanguages) {
        this.softwareLanguages = softwareLanguages;
    }

    public Set<String> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(Set<String> specialties) {
        this.specialties = specialties;
    }

    public Integer getEvaluationScore() {
        return evaluationScore;
    }

    public void setEvaluationScore(Integer evaluationScore) {
        this.evaluationScore = evaluationScore;
    }

    public EvaluationStatus getEvaluationStatus() {
        return evaluationStatus;
    }

    public void setEvaluationStatus(EvaluationStatus evaluationStatus) {
        this.evaluationStatus = evaluationStatus;
    }

    public Map<String, String> getAdditionalFields() {
        return additionalFields;
    }

    public void setAdditionalFields(Map<String, String> additionalFields) {
        this.additionalFields = additionalFields;
    }
}
