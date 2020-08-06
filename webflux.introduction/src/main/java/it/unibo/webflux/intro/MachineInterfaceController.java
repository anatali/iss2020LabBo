package it.unibo.webflux.intro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.unibo.webflux.intro.ControllerUtils;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;


//https://www.booleanworld.com/curl-command-tutorial-examples


@RestController 	//annotates all the methods with @ResponseBody that embeds the return value in the body of HTTP answer
public class MachineInterfaceController { 
    @Value("${machine.logo}")
    String logo;
    private ControllerUtils ctrlUtil = new ControllerUtils();
	
    @Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
    
    public MachineInterfaceController() { }

  String applicationModelRep=" now I'm waiting ...";

  @GetMapping(value = "/api",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)	//even if no json?
  public String entry(Model model) {
	  return "MachineInterfaceController | " + applicationModelRep ;
  }
  
  @GetMapping(value = "/api/getfluxoflong" ) 
  public Flux<Long> fluxlong(Model model) {
	  System.out.println("------------------------------------- fluxlong (/api/getfluxoflong) ");	//DUE volte?
	  return  Flux.just(10L, 20L, 30L); 
	  //return ctrlUtil.generateFluxLimitedWithScheduler(  );
  }
  
  @GetMapping(value = "/api/fluxstring" ) 
  public Flux<String> flux(Model model) {
	  return  Flux.just("A", "B", "C "); 
  }
  
   @GetMapping(value = "/api/getflux",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)	
   public Flux<String> execgetflux(Model model) {
	   System.out.println("------------------------------------- execgetflux (/api/getflux) ");
	   //return ctrlUtil.generateFluxLimitedWithScheduler( );  //does not terminates gracefully, but OK	   
	   DirectProcessor<String> hotsource = ctrlUtil.createHotSource(  );
	   int hotSourceNum = ctrlUtil.getElementCount();		//Java should provide a Pair ...
  	   ctrlUtil.populateHotFlux( hotsource, hotSourceNum );
	   return  hotsource.map( v -> v.toUpperCase() );
   } 

    public Flux<String> generateFlux0() {
	   return Flux.generate(
	 	() -> 0, 			//initial state value
	 	(n, sink) -> {
	 	  sink.next("generateFlux0 " + n); 
	 	  if (n == 10) sink.complete(); 
	 	  ctrlUtil.delay(1000);
	 	  return n + 1; 	//a new state for the next invocation
	 	});	
	 }   
   
    @ExceptionHandler 
    public ResponseEntity<String> handle(Exception ex) {
    	HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>(
        		"MachineInterfaceController ERROR " + ex.getMessage(), responseHeaders, HttpStatus.CREATED);
    }
 
}

