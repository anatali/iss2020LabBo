package it.unibo.websocket.intro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@SpringBootApplication
public class ApplicationWebsocket {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationWebsocket.class, args);
	}

	@Bean
	public SpringResourceTemplateResolver thymeleafTemplateResolver() {
		final SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setPrefix("classpath:templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		return resolver;
	}
}
