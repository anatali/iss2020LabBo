package it.unibo.websocket.intro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
 

//@SpringBootApplication
public class ApplicationWebsocket {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationWebsocket.class, args);
	}
/*
	@Bean
	public SpringResourceTemplateResolver thymeleafTemplateResolver() {
		final SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setPrefix("classpath:templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		return resolver;
	}
*/	
}
