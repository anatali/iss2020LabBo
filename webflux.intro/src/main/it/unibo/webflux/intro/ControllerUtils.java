package it.unibo.webflux.intro;


import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.util.HtmlUtils;

import reactor.core.publisher.DirectProcessor;

public class ControllerUtils {
 	private  int elCount = 0;
	private  SimpMessagingTemplate simpMessagingTemplate = null;
 	
	public void setSimpMessagingTemplate( SimpMessagingTemplate t ) {
		this.simpMessagingTemplate = t;
	}
 	//https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/messaging/simp/SimpMessagingTemplate.html
 	//https://www.baeldung.com/spring-websockets-send-message-to-user
 	//https://www.toptal.com/java/stomp-spring-boot-websocket
	public  void sendMsgToGui(  String msg ) {
		System.out.println("sendMsgToGui msg=" + msg + " simpMessagingTemplate=" + simpMessagingTemplate );
		if( simpMessagingTemplate != null )
			simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, new ResourceRep( HtmlUtils.htmlEscape( msg )  ));		
	}


	public DirectProcessor<String> createHotSource(SimpMessagingTemplate t) {
		this.simpMessagingTemplate = t;
		DirectProcessor<String> hotSource = DirectProcessor.create();
//		Flux<String> hotFlux              = hotSource.map( String::toUpperCase ) ;  //
		int myn = ++elCount;   //a new hotflux
		hotSource.subscribe( 
				v     -> sendMsgToGui( v ) ,
				error -> System.out.println("autonomousupdateflux error= " + error ),
				()    -> sendMsgToGui( "hotSource_" + myn +" ends" )
		); 
		System.out.println("createHotSource DONE");
		return hotSource;
 	}
	
	public   void populateHotFlux( DirectProcessor<String> hotSource ) {
		int myn = elCount;    
		new Thread() {
 			public void run() {
				for( int i=0; i<10; i++) {
					hotSource.onNext("hotSource_" + myn +" emits value= " + i);	
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
