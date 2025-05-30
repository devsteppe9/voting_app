package com.cs544.vote_session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = { "com.cs544" })
public class VoteSessionApplication {

	public static void main(String[] args) {
		System.out.println("Hello from vote-session!");
		SpringApplication.run(VoteSessionApplication.class, args);
	}

}
