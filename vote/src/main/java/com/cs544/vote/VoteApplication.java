package com.cs544.vote;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class VoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoteApplication.class, args);
		System.out.println("Just to test new deployment");
	}

	@Bean
	public NewTopic hello() {
		return TopicBuilder.name("voter").build();
	}

	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
