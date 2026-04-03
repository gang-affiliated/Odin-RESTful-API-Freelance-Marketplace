package com.odine.Freelance.Marketplace.API.freelancer.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "freelancers")
public class Freelancer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FreelancerType freelancerType;

    @Column
    private String portfolioUrl;

    @ElementCollection
    @CollectionTable(name = "freelancer_design_tools", joinColumns = @JoinColumn(name = "freelancer_id"))
    @Column(name = "tool_name")
    private Set<String> designTools = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "freelancer_languages", joinColumns = @JoinColumn(name = "freelancer_id"))
    @Column(name = "language_name")
    private Set<String> softwareLanguages = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "freelancer_specialties", joinColumns = @JoinColumn(name = "freelancer_id"))
    @Column(name = "specialty_name")
    private Set<String> specialties = new HashSet<>();

    @Column
    private Integer evaluationScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EvaluationStatus evaluationStatus = EvaluationStatus.PENDING;

    @ElementCollection
    @CollectionTable(name = "freelancer_additional_fields", joinColumns = @JoinColumn(name = "freelancer_id"))
    @MapKeyColumn(name = "field_key")
    @Column(name = "field_value")
    private Map<String, String> additionalFields = new HashMap<>();

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
