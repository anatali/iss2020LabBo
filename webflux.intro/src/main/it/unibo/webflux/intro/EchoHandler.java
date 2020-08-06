package it.unibo.webflux.intro;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

import it.unibo.webflux.utils.ControllerUtils;
import reactor.core.publisher.Mono;

public class EchoHandler implements WebSocketHandler 
{
	@Override
	public Mono<Void> handle(WebSocketSession session) 
	{
		return session
				.send( session.receive()
								//.map(msg -> "RECEIVED ON SERVER :: " + elab( msg.getPayloadAsText() ) )
								.map(msg -> msg.getPayloadAsText().toUpperCase() )
								.map(session::textMessage) 
					);
	}
	
	
//BY AN
	private ControllerUtils ctrlUtil = new ControllerUtils();
	
	private void startautonomousupdate(   ) {
 		for( int i=0; i<5; i++) {
 			delay(1000);
		}
	}
	
	private String elab( String msg ) {
		return msg + " (now elab)";
	}

	public  void delay(int dt) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}


}
