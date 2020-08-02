package it.unibo.webflux.intro;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import it.unibo.robotWeb2020.ResourceRep;
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
//    @ResponseBody
    public String entry(Model model) {
    	return "robotGuiSocket";  
    }

	@GetMapping("/gui")
//    @ResponseBody
    public Publisher<String> showGui() {
    	return Mono.just( "robotGuiSocket" );
    }

	@GetMapping("/todo")
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
	
	
    /*
     * Update the page vie socket.io when the application-resource changes.
     * Thanks to Eugenio Cerulo
     */
    	@Autowired
    	SimpMessagingTemplate simpMessagingTemplate;

    	@MessageMapping("/update")
    	@SendTo("/topic/display")
    	public ResourceRep updateTheMap(@Payload String message) {
    		ResourceRep rep = new ResourceRep("" + HtmlUtils.htmlEscape("hello from updateTheMap")  ); //getWebPageRep();
    		return rep;
    	}
    	
	private void delay(int dt) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}
    
}