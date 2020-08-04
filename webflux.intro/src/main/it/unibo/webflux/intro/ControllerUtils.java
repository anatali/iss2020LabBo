package it.unibo.webflux.intro;


import java.time.Duration;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.util.HtmlUtils;

import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class ControllerUtils {
 	private  int elementCount = 0;
//	private  SimpMessagingTemplate simpMessagingTemplate = null;
 	
//	public void setSimpMessagingTemplate( SimpMessagingTemplate t ) {
//		this.simpMessagingTemplate = t;
//	}
 	
 	public int getElementCount() {
 		return elementCount;
 	}
 	//https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/messaging/simp/SimpMessagingTemplate.html
 	//https://www.baeldung.com/spring-websockets-send-message-to-user
 	//https://www.toptal.com/java/stomp-spring-boot-websocket
	public  void sendMsgToGui(  SimpMessagingTemplate simpMessagingTemplate, String msg ) {
		System.out.println("sendMsgToGui msg=" + msg + " simpMessagingTemplate=" + simpMessagingTemplate );
		if( simpMessagingTemplate != null )
			simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, new ResourceRep( HtmlUtils.htmlEscape( msg )  ));		
	}
	
	
	   public Flux<Long> generateFluxLimitedWithScheduler(  ) {
		   Scheduler disiScheduler = Schedulers.newSingle("disiScheduler");
		   Flux<Long> flux = Flux.interval( Duration.ofMillis(1000 ), disiScheduler ) 
		   	        .map( tick -> {if (tick <= 6) return tick; else { disiScheduler.dispose(); return tick;}  } );
		   return flux;
		   //flux.subscribe( v -> sendMsgToGui( simpMessagingTemplate, "flux update " + v ) );
	   }
	   
	   public Flux<Long> generateHotFluxLimited (  ) {
		   Scheduler disiScheduler = Schedulers.newSingle("disiScheduler");
		   Flux<Long> flux = Flux.interval( Duration.ofMillis(1000 ), disiScheduler ) 
		   	        .map( tick -> {if (tick <= 6) return tick; else { disiScheduler.dispose(); return tick;}  } );
		   return flux;
		   //flux.subscribe( v -> sendMsgToGui( simpMessagingTemplate, "flux update " + v ) );
	   }
	   
	/*
	 * Updates the GUI page by using the socket
	 */
	public void startautonomousupdate( SimpMessagingTemplate simpMessagingTemplate ) {
 		new Thread() {
			int n = 0;
			public void run() {
				for( int i=0; i<5; i++) {
					sendMsgToGui( simpMessagingTemplate, "autonomous update " + n++ );
					delay(1000);
				}
			}
		}.start();		
	}

	public DirectProcessor<String> createHotSource( ) {
 		DirectProcessor<String> hotSource = DirectProcessor.create();
		++elementCount;   	              //a new element is born
		return hotSource;
 	}
	
	public void populateHotFlux( DirectProcessor<String> hotSource, int myn ) {
 		new Thread() {
 			public void run() {
				for( int i=0; i<10; i++) {
					hotSource.onNext("hotSource_" + myn +" emits value= " + i+"\n");	
					System.out.println("populateHotFlux next= " + i);
					delay( 1000 + myn*50);
				}
				hotSource.onComplete();		
			}
		}.start();		
		System.out.println("populateHotFlux STARTED");
	}
	
	public  void delay(int dt) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}
	
}
