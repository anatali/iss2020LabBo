package  learningstep0;
 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;

// tag::code[]
//@SpringBootApplication
public class ApplicationStep0 {

	public static void main(String[] args) {
//		System.setProperty("spring.devtools.restart.enabled", "false"); //By AN: to avoid SilentExitException in Eclipse

		SpringApplication.run(
			ApplicationStep0.class, args);
	}

	@Bean
	HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}

}
// end::code[]

 