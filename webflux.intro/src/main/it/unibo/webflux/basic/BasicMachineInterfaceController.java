package it.unibo.webflux.basic;

 
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
 

@RestController
public class BasicMachineInterfaceController {  //Book pg.17

	private final ChapterRepository repository;

	public BasicMachineInterfaceController(ChapterRepository repository) {
		this.repository = repository;
	}
	
	
	//http://localhost:8082/api 
//	@GetMapping("/api")
//	// @ResponseBody  //CAN BE OMITTED
//    public String entry( ) {
//      	return "BasicMachineInterfaceController - entry ";  
//    }
	
	//http://localhost:8082/api/greeting  or http://localhost:8082/api/greeting?name=Bob
	@GetMapping("/api/greeting")
	public String greeting(@RequestParam(required = false, defaultValue = "User") String name) {
		return name.equals("")
			? "Hey | from BasicMachineInterfaceController - greeting "
			: "Hey, " + name + " | from BasicMachineInterfaceController - greeting";
	}
	
	//http://localhost:8082/api/mono
	@GetMapping("/")
	public Mono<String> mono(Model model) {
		String msg = "Welcome from BasicMachineInterfaceController - mono ";
 		model.addAttribute("applLogo",msg);
		return Mono.just("index");
	}

	@GetMapping("/api/chapters")
	public Flux<Chapter> listing() {
		return repository.findAll();
	}
	
}
