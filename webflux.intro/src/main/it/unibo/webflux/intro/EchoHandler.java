package it.unibo.webflux.intro;


import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import it.unibo.webflux.utils.ControllerUtils;
import reactor.core.publisher.Mono;

@Component
public class EchoHandler implements WebSocketHandler 
{
	@Override
	public Mono<Void> handle(WebSocketSession session) 
	{
		return session
				.send( session.receive()
								//.map(msg -> "RECEIVED ON SERVER :: " + elab( msg.getPayloadAsText() ) )
								.map(msg -> msg.getPayloadAsText().toUpperCase() )
								.map(msg -> elab( msg, session ) )
								.map(session::textMessage) 
					);
	}
	
	
//BY AN
	private ControllerUtils ctrlUtil = new ControllerUtils();
	
	private void startautonomousupdate(   ) {
 		for( int i=0; i<5; i++) {
 			ctrlUtil.delay(1000);
		}
	}
	
	private String elab( String msg, WebSocketSession session ) {
		return msg + " (elab session="+ session +")";
	}

 

}
