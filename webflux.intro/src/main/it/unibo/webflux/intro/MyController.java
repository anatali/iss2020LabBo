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
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class MyController {
//	private DirectProcessor<String> hotSource = DirectProcessor.create();
//	private Flux<String> hotFlux              = hotSource.map(String::toUpperCase) ;
	private int n = 0;
	private String outS = "";

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;
	private void sendMsgToGui( String msg ) {
    	simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, new ResourceRep( HtmlUtils.htmlEscape( msg )  ));		
	}

//	private Thread updateThread = new Thread() {
//		int n = 0;
//		public void run() {
//			for( int i=0; i<5; i++) {
//				sendMsgToGui( "autonomous update " + n++ );
//		    	delay(1000);
//			}
//		}
//	};
	
//	private Thread genHotThread = new Thread() {
//		int n = 0;
//		public void run() {
//			hotFlux.subscribe( 
//					v     -> sendMsgToGui(v) ,
//					error -> System.out.println("autonomousupdateflux error= " + error ),
//					()    -> System.out.println("autonomousupdateflux END " )
//			);
//			for( int i=0; i<10; i++) {
//				hotSource.onNext("hotValue " + n++);		    	
//				delay(1000);
//			}
//			hotSource.onComplete();		
//		}
//	};

	@GetMapping("/")
//    @ResponseBody
    public String entry(Model model) {
    	return "robotGuiSocket";  
    }

	@GetMapping("/update")
//  @ResponseBody
  public String autonomousupdate(Model model) {
		new Thread() {
			int n = 0;
			public void run() {
				for( int i=0; i<5; i++) {
					sendMsgToGui( "autonomous update " + n++ );
			    	delay(1000);
				}
			}
		}.start();
  	return "robotGuiSocket";  
  }
	
	
 	
	@GetMapping("/updateflux")
//  @ResponseBody
  public String autonomousupdateflux(Model model) {
		new Thread() {
			public void run() {
				DirectProcessor<String> hotSource = DirectProcessor.create();
				Flux<String> hotFlux              = hotSource.map(String::toUpperCase) ;
				int myn = ++n;   //a new hotflux
				hotFlux.subscribe( 
						v     -> sendMsgToGui(v) ,
						error -> System.out.println("autonomousupdateflux error= " + error ),
						()    -> System.out.println("autonomousupdateflux END " )
				);
				for( int i=0; i<10; i++) {
					hotSource.onNext("hotFlux_" + myn +" value= " + i);		    	
					delay(1000);
				}
				hotSource.onComplete();		
			}
		}.start();
  	return "robotGuiSocket";  
  }
	
	@GetMapping("/gui")
//    @ResponseBody
    public Publisher<String> showGui() {
    	return Mono.just( "robotGuiSocket" );
    }

//	@GetMapping("/todo")
//    @ResponseBody
//    public Publisher<String> handler() {
//		hotFlux.subscribe( d -> outS = outS + " | " + d );
//		buildAnswer();
//		delay(3000); 
//   		return Mono.just( outS );
//    }
    
   
//	public void buildAnswer() {
//   		hotFlux.subscribe(d -> System.out.println("Local observer: "+d));	 
//  		for( int i= 0; i<5; i++ ) { 
//  			delay(1000); 
//  			hotSource.onNext("value"+ n++); 
//  		}
//		hotSource.onComplete();		
// 	}
	
	
    /*
     * Update the page via socket.io. Thanks to Eugenio Cerulo
     */

    	@MessageMapping("/update")
    	@SendTo("/topic/display")
    	public ResourceRep updateTheMap(@Payload String message) {	//The receiver will look at a field named content
    		ResourceRep rep = new ResourceRep("" + HtmlUtils.htmlEscape("hello from updateTheMap for message=" + message)  ); //getWebPageRep();
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