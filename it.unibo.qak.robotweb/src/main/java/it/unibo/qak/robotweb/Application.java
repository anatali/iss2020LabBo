package it.unibo.qak.robotweb;

/*
 * See https://www.baeldung.com/spring-boot-change-port
 * https://howtodoinjava.com/spring-webflux/spring-webflux-tutorial/
 * https://docs.spring.io/spring-framework/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/web-reactive.html
 * BAD REQUEST https://www.ionos.it/digitalguide/hosting/tecniche-hosting/messaggio-di-errore-http-400-alla-ricerca-delle-cause/
 */

 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/*
 * The @SpringBootApplication annotation is equivalent to using 
 * @Configuration, @EnableAutoConfiguration and @ComponentScan 
 * with their default attributes
 */
@SpringBootApplication
public class Application {

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		
//		AWebClient gwc = new AWebClient();
//		System.out.println(gwc.getResult());
 	}
 
}
