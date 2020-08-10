package it.unibo.webflux.social;
 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;


// tag::code[]
@SpringBootApplication		 
public class ApplicationSocial {  //Book pg. 

	public static void main(String[] args) {
 		//System.setProperty("spring.devtools.restart.enabled", "false"); //By AN: to avoid SilentExitException in Eclipse

		SpringApplication.run(
			ApplicationSocial.class, args);
	}

	@Bean
	HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}

}
// end::code[]

 