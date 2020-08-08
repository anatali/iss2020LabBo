package it.unibo.websocket.intro;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono; 

@Component
public class WebSocketHandlers implements WebSocketHandler{
	private int count = 1;
     
    public Flux<String> handleStream(String v){
    	//System.out.println("handleStream count=" + count + " v=" + v );
    	if( count++ %2 == 0 ) return hotSourceData(count); else return reverseString(v);   		
    }

    public Flux<String> reverseString(String userInput){
    	return Flux.just(new StringBuilder(userInput).reverse().toString());
     }
 
    public Flux<String> hotSourceData(int count){
     	DirectProcessor<String> hotSource = DirectProcessor.create();
    	Flux<String> result = hotSource.map(String::toUpperCase);
 	        new Thread(){
	        	public void run() {
	        		 System.out.println("hotSourceData start " + count);
	        		 for( int i=1; i<=3;i++) {
	        			 hotSource.onNext("item_"+ count+"_"+i);
	        			 delay(500);
	        		 }
 	           	 	 hotSource.onComplete();			 
	        		 System.out.println("hotSourceData ends " + count);
	        	}
	         }.start();
        return result; 
    }
      
    @Override
    public Mono<Void> handle(WebSocketSession session) {
      return session.send(session.receive()
              .map(WebSocketMessage::getPayloadAsText)
              .flatMap( this::handleStream    )
              .map(v -> session.textMessage(v))); 
    } 
     
     
	public  void delay(int dt) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}

}
