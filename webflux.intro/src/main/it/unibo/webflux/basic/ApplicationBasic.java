package it.unibo.webflux.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

//@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class ApplicationBasic {	 //Book pg.14

	public static void main(String[] args) {
		SpringApplication.run(ApplicationBasic.class, args); 
	}
}
