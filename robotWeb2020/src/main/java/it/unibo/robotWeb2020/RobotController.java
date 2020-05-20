package it.unibo.robotWeb2020;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value; 
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import connQak.ConnectionType;
import connQak.connQakBase;
import connQak.connQakCoap;
import connQak.sysConnKb;
 

@Controller 
public class RobotController { 
    String appName ="robotGui";
    
    Set<String> robotMoves = new HashSet<String>(); 
    
    connQakBase connQakSupport ; // connQak.connQakBase.create("", "", "","" ) ;
    
    public RobotController() {
        robotMoves.addAll( Arrays.asList(new String[] {"w","s","h","r","l","z","x"}) );      
        connQakSupport = new connQakCoap("localhost", "8014","virtualrobot"); 

// 						connQak.sysConnKb.ConnectionType.COAP,
// 						connQak.sysConnKb.hostAddr, 
// 						connQak.SysConnKbKt.port, 
// 						connQak.SysConnKbKt.qakdestination  );
//		 connQakSupport.createConnection()

    }

  String applicationModelRep="waiting";

   @GetMapping("/") 		 
  public String entry(Model model) {
       model.addAttribute("arg", "Initial Robot State: "+applicationModelRep);
      return "index"; 
  } 
   
  @GetMapping("/model")
  @ResponseBody
  public String halt(Model model) {
	  model.addAttribute("arg", appName);
      return String.format("Current Robot State:  " + applicationModelRep );      
  }     
	
	
 
	@PostMapping( path = "/move" ) 
	public String doMove( 
		@RequestParam(name="move", required=false, defaultValue="h") 
		//binds the value of the query string parameter name into the moveName parameter of the  method
		String moveName, Model viewmodel) {
		  System.out.println("------------------- RobotController doMove move=" + moveName  );
		if( robotMoves.contains(moveName) ) {
			applicationModelRep = moveName;
			viewmodel.addAttribute("arg", "Current Robot State:  "+applicationModelRep);
		}else {
			viewmodel.addAttribute("arg", "Sorry: move unknown - Current Robot State:"+applicationModelRep );
		}		
		return "index";
	}	
    
 	

    @ExceptionHandler 
    public ResponseEntity<String> handle(Exception ex) {
    	HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>(
        		"RobotController ERROR " + ex.getMessage(), responseHeaders, HttpStatus.CREATED);
    }

}

