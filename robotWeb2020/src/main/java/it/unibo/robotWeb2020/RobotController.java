package it.unibo.robotWeb2020;
//https://www.baeldung.com/websockets-spring
//https://www.toptal.com/java/stomp-spring-boot-websocket
	
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import connQak.configurator;
import connQak.connQakCoap;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;


@Controller 
public class RobotController { 
    String appName     ="robotGui";
    String viewModelRep="startup";
    String robotHost = ""; //ConnConfig.hostAddr;		
    String robotPort = ""; //ConnConfig.port;
     
    //String htmlPage  = "robotGuiPost"; 
    String htmlPage  = "robotGuiSocket";		//see constructor
    //String htmlPage  = "robotGuiPostBoundary"; 
    
    Set<String> robotMoves = new HashSet<String>(); 
    
    connQakCoap connQakSupport ;   
    
    public RobotController() {
        connQak.configurator.configure();
        htmlPage  = connQak.configurator.getPageTemplate();
        robotHost =	connQak.configurator.getHostAddr();	
        robotPort = connQak.configurator.getPort();

        robotMoves.addAll( Arrays.asList(new String[] {"w","s","h","r","l","z","x","p"}) );       
        connQakSupport = new connQakCoap(  );  
        connQakSupport.createConnection();
          
     }

    /*
     * Update the page vie socket.io when the application-resource changes.
     * Thanks to Eugenio Cerulo
     */
    	@Autowired
    	SimpMessagingTemplate simpMessagingTemplate;

  @GetMapping("/") 		 
  public String entry(Model viewmodel) {
 	 viewmodel.addAttribute("arg", "Entry page loaded. Please use the buttons ");
 	 peparePageUpdating();
 	 return htmlPage;
  } 
   
  @GetMapping("/applmodel")
  @ResponseBody
  public String getApplicationModel(Model viewmodel) {
  	  ResourceRep rep = getWebPageRep();
	  return rep.getContent();
  }     
  
    
	@PostMapping( path = "/move" ) 
	public String doMove( 
		@RequestParam(name="move", required=false, defaultValue="h") 
		//binds the value of the query string parameter name into the moveName parameter of the  method
		String moveName, Model viewmodel) {
		System.out.println("------------------- RobotController doMove move=" + moveName  );
		if( robotMoves.contains(moveName) ) {
			doBusinessJob(moveName, viewmodel);
 		}else {
			viewmodel.addAttribute("arg", "Sorry: move unknown - Current Robot State:"+viewModelRep );
		}		
		return htmlPage;
		//return "robotGuiSocket";  //ESPERIMENTO
	}	
	
 
	private void peparePageUpdating() {
    	connQakSupport.getClient().observe(new CoapHandler() {
			@Override
			public void onLoad(CoapResponse response) {
				System.out.println("RobotController --> CoapClient changed ->" + response.getResponseText());
				simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, 
						new ResourceRep("" + HtmlUtils.htmlEscape(response.getResponseText())  ));
			}

			@Override
			public void onError() {
				System.out.println("RobotController --> CoapClient error!");
			}
		});
	}
	
	/*
	 * INTERACTION WITH THE BUSINESS LOGIC			
	 */
	protected void doBusinessJob( String moveName, Model viewmodel) {
		try {
			if( moveName.equals("p")) {
				ApplMessage msg = MsgUtil.buildRequest("web", "step", "step("+configurator.getStepsize()+")", configurator.getQakdest() );
				connQakSupport.request( msg );				
			}
			else {
				ApplMessage msg = MsgUtil.buildDispatch("web", "cmd", "cmd("+moveName+")", configurator.getQakdest() );
				connQakSupport.forward( msg );
			}		
			//WAIT for command completion ...
			Thread.sleep(400);  //QUITE A LONG TIME ...
			if( viewmodel != null ) {
				ResourceRep rep = getWebPageRep();
				viewmodel.addAttribute("arg", "Current Robot State:  "+rep.getContent());
			}
		} catch (Exception e) {
			System.out.println("------------------- RobotController doBusinessJob ERROR=" + e.getMessage()  );
			//e.printStackTrace();
		}		
	}

    @ExceptionHandler 
    public ResponseEntity<String> handle(Exception ex) {
    	HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>(
        		"RobotController ERROR " + ex.getMessage(), responseHeaders, HttpStatus.CREATED);
    }

/* ----------------------------------------------------------
   Message-handling Controller
  ----------------------------------------------------------
 */
//	@MessageMapping("/hello")
//	@SendTo("/topic/display")
//	public ResourceRep greeting(RequestMessageOnSock message) throws Exception {
//		Thread.sleep(1000); // simulated delay
//		return new ResourceRep("Hello by AN, " + HtmlUtils.htmlEscape(message.getName()) + "!");
//	}
	
	@MessageMapping("/move")
 	@SendTo("/topic/display")
 	public ResourceRep backtoclient(RequestMessageOnSock message) throws Exception {
// 		ApplMessage msg = MsgUtil.buildDispatch("web", "cmd", "cmd("+message.getName()+")", "basicrobot" );
//		connQakSupport.forward( msg );
//		System.out.println("------------------- RobotController forward=" + msg  );
		doBusinessJob(message.getName(), null);
//		//WAIT for command completion ...
//		Thread.sleep(400);
		return getWebPageRep();
 	}
	
	@MessageMapping("/update")
	@SendTo("/topic/display")
	public ResourceRep updateTheMap(@Payload String message) {
		ResourceRep rep = getWebPageRep();
		return rep;
	}

	public ResourceRep getWebPageRep()   {
		String resourceRep = connQakSupport.readRep();
		System.out.println("------------------- RobotController resourceRep=" + resourceRep  );
		return new ResourceRep("" + HtmlUtils.htmlEscape(resourceRep)  );
	}
	
  
 
	
 
/*
 * The @MessageMapping annotation ensures that, 
 * if a message is sent to the /hello destination, the greeting() method is called.    
 * The payload of the message is bound to a HelloMessage object, which is passed into greeting().
 * 
 * Internally, the implementation of the method simulates a processing delay by causing 
 * the thread to sleep for one second. 
 * This is to demonstrate that, after the client sends a message, 
 * the server can take as long as it needs to asynchronously process the message. 
 * The client can continue with whatever work it needs to do without waiting for the response.
 * 
 * After the one-second delay, the greeting() method creates a Greeting object and returns it. 
 * The return value is broadcast to all subscribers of /topic/display, 
 * as specified in the @SendTo annotation. 
 * Note that the name from the input message is sanitized, since, in this case, 
 * it will be echoed back and re-rendered in the browser DOM on the client side.
 */
    
 
/*
 * curl --location --request POST 'http://localhost:8080/move' --header 'Content-Type: text/plain' --form 'move=l'	
 * curl -d move=r localhost:8080/move
 */
}

