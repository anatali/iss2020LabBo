package it.unibo.webflux.intro;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.HtmlUtils;

import it.unibo.webflux.utils.ControllerUtils;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Controller
public class HumanInterfaceController {

	private ControllerUtils ctrlUtil = new ControllerUtils();
	
//	@Autowired
//	private SimpMessagingTemplate simpMessagingTemplate;
 
	@GetMapping("/")
	// @ResponseBody  //CAN BE OMITTED
    public String entry(Model model) {
		ctrlUtil.resetElementCount();
    	return "index";  
    }
	 
	@GetMapping("/show") 
    public String showResource(Model model) {
     	String msg = HtmlUtils.htmlEscape("GET | show some resource"  );  
     	model.addAttribute("show", msg);
     	return "robotGuiSocket";  
    }

//	@GetMapping("/update")
//    public String autonomousupdate() {
//		ctrlUtil.startautonomousupdate( simpMessagingTemplate );	//The page is updated via socket
//		return "robotGuiSocket";  //The page is the initial one
//    }
  
//	@GetMapping("/updatefromflux")	
//	public String autonomousupdateflux(Model model) {
//		DirectProcessor<String> hotSource =  ctrlUtil.createHotSource();
//		int hotSourceelnum = ctrlUtil.getElementCount();		//Java should provide a Pair ...
//		hotSource.subscribe( 
//				v     -> ctrlUtil.sendMsgToGui( simpMessagingTemplate, v ) ,
//				error -> System.out.println("autonomousupdateflux error= " + error ),
//				()    -> ctrlUtil.sendMsgToGui( simpMessagingTemplate, "hotSource_" + hotSourceelnum +" ends" )
//		); 
//		ctrlUtil.populateHotFlux( hotSource, hotSourceelnum );
//  	return "robotGuiSocket";  
//	}
	
	@GetMapping("/gui")
    public Publisher<String> showGui() {
    	return Mono.just( "robotGuiSocket" );
    }

	
	
    /*
     * Update the page via socket.io. Thanks to Eugenio Cerulo
     * https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/messaging/handler/annotation/MessageMapping.html
     * https://spring.io/guides/gs/messaging-stomp-websocket/
     */

/*	
    	@MessageMapping("/showresource")
       	@SendTo( WebSocketConfig.topicForClient )  
    	public ResourceRep showresource(@Payload String message) {	//The receiver will look at a field named content
    		ctrlUtil.incElementCount();
    		ResourceRep rep = new ResourceRep( HtmlUtils.htmlEscape("socket | HELLO "+ ctrlUtil.getElementCount() +" for message=" + message)  );  
    		return rep;
    	}
    	
    	@MessageMapping("/startresourceupdating")
    	@SendTo( WebSocketConfig.topicForClient )
    	public ResourceRep startresourceupdating(@Payload String message) {	//The receiver will look at a field named content
    		ResourceRep rep = new ResourceRep( HtmlUtils.htmlEscape("socket | BYE for message=" + message)  );  
    		ctrlUtil.startautonomousupdate( simpMessagingTemplate );	//synch
    		return rep;
    	}
 
    	@MessageMapping("/resourceflux") 
    	@SendTo(  WebSocketConfig.topicForClient )
     	public ResourceRep startresourceflux(@Payload String message) {	//The receiver will look at a field named content
    		Flux<Long> flux = ctrlUtil.generateFluxLimitedWithScheduler();
    		flux.subscribe( v -> ctrlUtil.sendMsgToGui( simpMessagingTemplate, "HumanGui_"+ctrlUtil.getElementCount()+" | flux-value=" + v ) );
    		ResourceRep rep = new ResourceRep( HtmlUtils.htmlEscape("socket | SENDING for message=" + message)  );  
    		return rep;
    	}
*/    
}