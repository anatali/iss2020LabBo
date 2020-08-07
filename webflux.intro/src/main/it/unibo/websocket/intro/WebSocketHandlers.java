package it.unibo.websocket.intro;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.Disposable;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
 
 

@Component
public class WebSocketHandlers implements WebSocketHandler
{
    private Flux<String> result = Flux.just("");
    private DirectProcessor<String> hotSource = DirectProcessor.create();
    
    public Flux<String> reverseString(String userInput){
        result = Flux.just(new StringBuilder(userInput).reverse().toString());
        return result; 
    }
 
//    public Flux<String> hotSource(String userInput){
//         result = Flux.just("A" , "B",  userInput); 
//         return result; 
//    }

    public Flux<String> hotSourceData(String userInput)
    {
     	result = hotSource.map(String::toUpperCase);
        new Thread(){
        	public void run() {
        		demoHot0("aaa"); 
        	}
         }.start();
        return result; 
    }
    
    public void demoHot0(String msg) {
 System.out.println("demoHot0");
    	 hotSource.onNext(msg);delay(1000);
 System.out.println("demoHot0");
    	 hotSource.onNext("blue");delay(1000);
 System.out.println("demoHot0");
     	hotSource.onNext("green");delay(1000);
 System.out.println("demoHot0");
    	 hotSource.onNext("orange");delay(1000);
 System.out.println("demoHot0");
     	hotSource.onNext("purple");    delay(1000);	 
System.out.println("demoHot0 ends");
    	 hotSource.onComplete();		
    }
     
/*
 * The packet size is larger than the one set for Text/Binary message on Spring WebSocket handler.
 */
    @Override
    public Mono<Void> handle(WebSocketSession session) {
    	  //System.out.println("handle ------------------------------- "   );
//          return session.send(session.receive()
//                 .map(WebSocketMessage::getPayloadAsText)
//                 .flatMap(this::reverseString)
//                 .map(reversedString -> session.textMessage(reversedString))); //.subscribe(System.out::println);
    	  return handleHot(session); 
     }
    
    //@Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        session.setTextMessageSizeLimit(1024 * 1024);
//        session.setBinaryMessageSizeLimit(1024 * 1024);
//        //log.info("Connection established");
//    }    
     

    
    public Mono<Void> handleHot(WebSocketSession session) {
    	 System.out.println("handleHot ------------------------------- "   );
    	 return session.send(session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(this::hotSourceData)
         		.map( str -> session.textMessage(str)) );//.subscribe(System.out::println) ;
    	  //return m.then();
        

//        return Mono.empty();
    }
   
    
	public  void delay(int dt) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}

}
