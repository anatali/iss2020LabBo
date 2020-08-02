package it.unibo.webflux.intro;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class MyController {
	private DirectProcessor<String> hotSource = DirectProcessor.create();
	private Flux<String> hotFlux              = hotSource.map(String::toUpperCase) ;
	private int n = 0;
	private String outS = "";
      
	@GetMapping("/")
    @ResponseBody
    public Publisher<String> handler() {
		hotFlux.subscribe( d -> outS = outS + " | " + d );
		buildAnswer();
		delay(3000); 
   		return Mono.just( outS );
    }
    
    
	public void buildAnswer() {
   		hotFlux.subscribe(d -> System.out.println("Local observer: "+d));	 
  		for( int i= 0; i<5; i++ ) { 
  			delay(1000); 
  			hotSource.onNext("value"+ n++); 
  		}
		hotSource.onComplete();		
 	}
	
	private void delay(int dt) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}
    
}