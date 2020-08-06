package com.greglturnquist.learningspringboot.part1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

//@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class LearningSpringBootApplication {	 //Book pg.14

	public static void main(String[] args) {
		SpringApplication.run(
			LearningSpringBootApplication.class, args);
	}
}
