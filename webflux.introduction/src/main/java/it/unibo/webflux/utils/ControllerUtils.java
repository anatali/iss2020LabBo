package it.unibo.webflux.utils;

import java.time.Duration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.util.HtmlUtils;
import it.unibo.webflux.intro.ResourceRep;
import it.unibo.webflux.intro.WebSocketConfig;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class ControllerUtils {
 	private  int elementCount = 0;
 	
 	public void resetElementCount() {
 		 elementCount = 0;
 	}
 	public void incElementCount() {
		 elementCount++;
	}
	public int getElementCount() {
 		return elementCount;
 	}
 	//https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/messaging/simp/SimpMessagingTemplate.html
 	//https://www.baeldung.com/spring-websockets-send-message-to-user
 	//https://www.toptal.com/java/stomp-spring-boot-websocket
	public  void sendMsgToGui(  SimpMessagingTemplate simpMessagingTemplate, String msg ) {
//		System.out.println("ControllerUtils | sendMsgToGui msg=" + msg + " simpMessagingTemplate=" + simpMessagingTemplate );
		if( simpMessagingTemplate != null )
			simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, new ResourceRep( HtmlUtils.htmlEscape( msg )  ));		
	}
	
	
	   public Flux<Long> generateFluxLimitedWithScheduler(  ) {
		   incElementCount();
		   Scheduler disiScheduler = Schedulers.newSingle("disiScheduler");
		   //delay(1000); //give some time to show SENDING ...
		   Flux<Long> flux = Flux.interval( Duration.ofMillis(1000 ), disiScheduler ) 	//HOT
		   	        .map( tick -> {if (tick <= 6) return tick; else { disiScheduler.dispose(); return tick;}  } );
		   return flux;
 	   }
	   
	/*
	 * Updates the GUI page by using the socket
	 */
	public void startautonomousupdate( SimpMessagingTemplate simpMessagingTemplate ) {
		incElementCount();
		for( int i=0; i<5; i++) {
			sendMsgToGui( simpMessagingTemplate, "autonomous_ " + elementCount + " emits: " + i);
			delay(1000);
		}
	}

	public DirectProcessor<String> createHotSource( ) {
 		DirectProcessor<String> hotSource = DirectProcessor.create();
 		incElementCount();   	              //a new element is born
 		System.out.println("ControllerUtils |  createHotSource ------------------------------- "+ getElementCount() );
		return hotSource;
 	}
	
	public void populateHotFlux( DirectProcessor<String> hotSource, int myn ) {
// 		new Thread() {
// 			public void run() {
 				for( int i=0; i<10; i++) {
					hotSource.onNext("hotSource_" + myn +" emits value= " + i+"\n");	
					System.out.println("ControllerUtils | populateHotFlux " + getElementCount() + "/" + myn + " next= " + i);
					delay( 1000 + myn*50);
				}
				hotSource.onComplete();		 
//			}
//		}.start();		
//		System.out.println("ControllerUtils | populateHotFlux STARTED");
	}
	
	public  void delay(int dt) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}
	
}
