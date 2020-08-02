package it.unibo.webflux.intro;
 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
//@EnableWebFlux		//AVOID!!
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	 
//	 @Bean
//	    public SecurityWebFilterChain functionalValidationsSpringSecurityFilterChain(ServerHttpSecurity http) {
//	        http.authorizeExchange()
//	            .anyExchange()
//	            .permitAll();
//	        http.csrf().disable();
//	        return http.build();
//	    }	
	     
}
