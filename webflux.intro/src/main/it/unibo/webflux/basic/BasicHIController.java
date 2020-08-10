package it.unibo.webflux.basic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;
import reactor.core.publisher.Flux;

 
 

@Controller
public class BasicHIController {  //Book pg.17
	 
	//http://localhost:8082
	@GetMapping("/")
	//@ResponseBody  //CAN BE OMITTED
    public String entry(Model model) {
		String msg = HtmlUtils.htmlEscape("Welcome from | BasicHIController - entry ");
		model.addAttribute("applLogo",msg);
     	return "indexBasic";  
    }
	
	//http://localhost:8082/greeting?name=Bob
	@GetMapping("/greeting")
	public String greeting(Model model, @RequestParam(required = false, defaultValue = "User") String name) {
		String msg = "Welcome," + name + " from BasicHIController - greeting ";
		model.addAttribute("applLogo",msg);
		return "indexBasic";  
	}

	
}
