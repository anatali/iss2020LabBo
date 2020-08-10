package it.unibo.webflux.sse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
 

@Controller
public class SseHIController {

	@GetMapping("/")
	//@ResponseBody  //CAN BE OMITTED
    public String entry(Model model) {
     	return "indexSSE";   
    }
	
}
