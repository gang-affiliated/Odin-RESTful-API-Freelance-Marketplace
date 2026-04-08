package com.odine.Freelance.Marketplace.API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FreelanceMarketplaceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FreelanceMarketplaceApiApplication.class, args);
	}

}
