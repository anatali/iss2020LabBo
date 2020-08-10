package it.unibo.webflux.basic;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
 
@RestController
public class BasicMIController {  //Book pg.17

	private final ChapterRepository repository;

	public BasicMIController(ChapterRepository repository) {
		this.repository = repository;
	}
		
	//http://localhost:8082/api 
	@GetMapping("/api")
    public String entry( ) {
      	return "BasicMIController - entry ";  
    }

	//http://localhost:8082/api/mono 
	@GetMapping("/api/mono")
	//@ResponseBody  //CAN BE OMITTED
	public Mono<String> mono(Model model) {
		String msg = "Welcome from BasicMIController - mono ";
 		model.addAttribute("applLogo",msg);
		return Mono.just("indexBasic");		//just a string, not rendered
	}

	//http://localhost:8082/api/greeting  or http://localhost:8082/api/greeting?name=Bob
	@GetMapping("/api/greeting")
	public String greeting(@RequestParam(required = false, defaultValue = "User") String name) {
		return name.equals("")
			? "Hey | from BasicMIController - greeting "
			: "Hey, " + name + " | from BasicMachineInterfaceController - greeting";
	}
	
	@GetMapping("/api/chapters")
	public Flux<Chapter> listing() {
		return repository.findAll();
	}
	
}
