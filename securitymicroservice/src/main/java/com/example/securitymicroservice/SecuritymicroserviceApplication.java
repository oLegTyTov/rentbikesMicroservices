package com.example.securitymicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class SecuritymicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecuritymicroserviceApplication.class, args);
	}

}
