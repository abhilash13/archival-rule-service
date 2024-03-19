package com.archive.ruleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ArchivalRuleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchivalRuleServiceApplication.class, args);
	}

}
