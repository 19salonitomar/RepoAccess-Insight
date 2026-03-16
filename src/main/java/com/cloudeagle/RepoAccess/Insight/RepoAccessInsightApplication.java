package com.cloudeagle.RepoAccess.Insight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RepoAccessInsightApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepoAccessInsightApplication.class, args);
	}

}
