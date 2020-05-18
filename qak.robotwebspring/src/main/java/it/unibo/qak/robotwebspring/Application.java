package it.unibo.qak.robotwebspring;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@Controller 
//@RequestMapping("/") 		//shortcut for @RequestMapping(method = RequestMethod.GET)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@GetMapping("/")   
	//REST API just return data in form of JSON or XML  because most of the REST clients are programs
    public String entry( ) {
    	return "welcome";
    }	
}
