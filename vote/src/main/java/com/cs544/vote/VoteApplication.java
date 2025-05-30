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
		System.out.println("Hello world!");
		SpringApplication.run(VoteApplication.class, args);
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
