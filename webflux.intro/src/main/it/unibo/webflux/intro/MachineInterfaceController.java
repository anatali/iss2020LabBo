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

  String applicationModelRep="  | now I'm waiting ...";

  @GetMapping(value = "/api",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public String entry(Model model) {
	  return "MachineInterfaceController | " + applicationModelRep ;
  }
  
  
  @GetMapping(value = "/api/fluxstring" )
  public Flux<String> flux(Model model) {
	  return  Flux.just("A", "B", "C "); 
  }
  
   @GetMapping(value = "/api/getflux",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)	
   public Flux<String> getflux(Model model) {
	   //return ctrlUtil.generateFluxLimitedWithScheduler( );  //does not terminates gracefully, but OK	   
	   DirectProcessor<String> hotsource = ctrlUtil.createHotSource(  );
	   int hotSourceNum = ctrlUtil.getElementCount();		//Java should provide a Pair ...
  	   ctrlUtil.populateHotFlux( hotsource, hotSourceNum );
	   return  hotsource.map( v -> v );
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

