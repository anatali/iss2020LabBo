package it.unibo.webflux.intro;


import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import it.unibo.webflux.utils.ControllerUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class HotSourceDataEmitter implements WebSocketHandler 
{
	@Override
	public Mono<Void> handle(WebSocketSession session) 
	{
		 session
				.send( session.receive()
								//.map(msg -> "RECEIVED ON SERVER :: " + elab( msg.getPayloadAsText() ) )
								.map(msg -> msg.getPayloadAsText().toUpperCase() )
								//.map(msg -> elab( msg, session ) )
								.map(msg -> session.textMessage(msg)))
								.subscribe(System.out::println) ;
								//.map(session::textMessage) 								 
					 
					return Mono.empty();
	}
	
	
//BY AN
	private ControllerUtils ctrlUtil = new ControllerUtils();
	
	private void startautonomousupdate(   ) {
 		for( int i=0; i<5; i++) {
 			ctrlUtil.delay(1000);
		}
	}
	
	private String elab( String msg, WebSocketSession session ) {
//		Flux.zip(
//			      numbers1,
//			      numbers2 
//			  ). 
 		Mono<Integer> v = Flux.fromArray( new Integer[] {1,2,3,4,5} ) 
		.last();
		return msg + " (HotSourceDataEmitter elab session="+ session +")";
	}

 
	
	  Flux<Integer> numbers1 = Flux
	      .range(1, 3);
	  
	  Flux<Integer> numbers2 = Flux
	      .range(4, 2);
	  
	  
}
