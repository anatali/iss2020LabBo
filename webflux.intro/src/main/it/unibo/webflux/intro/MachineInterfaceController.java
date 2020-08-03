package it.unibo.webflux.intro;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

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
 
  
   @GetMapping(value = "/api/updateflux",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
//  @ResponseBody
  public Flux getMapping(Model model) {
	   //return Flux.interval(Duration.ofMillis(300)).map(f -> "HI " + f);
	   return generateFlux1();
  }

   public Flux<Long> generateFlux1() {
	   Scheduler disiScheduler = Schedulers.newSingle("disiScheduler");
	   Flux<Long> flux = Flux.interval( Duration.ofMillis(500 ), disiScheduler ) 
	   	        .map( tick -> {if (tick <= 6) return tick; else disiScheduler.dispose(); return tick; } );
	   return flux;
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

