package com.odine.Freelance.Marketplace.API.freelancer.dto;

import com.odine.Freelance.Marketplace.API.freelancer.entity.FreelancerType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

public class FreelancerCreateRequest {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String city;

    @NotNull
    private FreelancerType freelancerType;

    private String portfolioUrl;
    private Set<String> designTools;
    private Set<String> softwareLanguages;
    private Set<String> specialties;
    private Map<String, String> additionalFields;

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

    public Map<String, String> getAdditionalFields() {
        return additionalFields;
    }

    public void setAdditionalFields(Map<String, String> additionalFields) {
        this.additionalFields = additionalFields;
    }
}
