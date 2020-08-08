package it.unibo.websocket.intro;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;


@RestController
public class WebSockHMController {
	private int count = 0;
	
 	@GetMapping( "/hotdata" )
    public Flux<String> genSourceData( ){
 		count++;
     	DirectProcessor<String> hotSource = DirectProcessor.create();
     	Flux<String> result               = hotSource.map(String::toUpperCase).map( v -> "["+ v + "]\n" );  
//		try {
	        new Thread(){
	        	public void run() {
	        		 System.out.println("genSourceData start " + count);
	        		 for( int i=1; i<=15;i++) {
	        			 try { Thread.sleep(500); } catch (InterruptedException e) {}	
	        			 hotSource.onNext("value_"+ count+"_"+i);
	        		 }
 	           	 	 hotSource.onComplete();			 
	        		 System.out.println("genSourceData ends " + count);
	        	}
	        }.start();
		    System.out.println("genSourceData return result count=" + count);
// 		}catch( Exception e) {
// 			System.out.println("genSourceData ERROR" + e.getMessage() );
// 		}
        return result; 
    }
}
