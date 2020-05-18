package it.unibo.webspring.intro;

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
import org.springframework.web.server.ServerWebExchange;
 
@Controller 
public class BaseController { 
    @Value("${spring.application.name}")
    String appName;

    String curState="waiting";
/*
 * GET   
 */    
  @GetMapping("/") 		 
  public String homePage(Model model) {
	  System.out.println("------------------- BaseController homePage " + model  );
      model.addAttribute("arg", appName);
      return "welcome";
   } 
  
  @GetMapping("/model")
  @ResponseBody
  public String halt(Model model) {
	  model.addAttribute("arg", appName);
      return String.format("BaseController text normal state= " + curState );      
  }     
	
	@GetMapping("/gui")
	public String gui(
			@RequestParam(name="name", required=false, defaultValue="World") 
			//binds the value of the query string parameter name into the name parameter of the greeting() method
			String name, Model model) {
		model.addAttribute("arg", name);
		return "gui";
	}
	
    @RequestMapping(value = "/w", 
    		method = RequestMethod.POST,
    		consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody String doMove( String move ) {
        return  "doMove APPLICATION_FORM_URLENCODED_VALUE move="+move; 
    }    

	@PostMapping( path = "/move" ) 
	public String doMove( 
		@RequestParam(name="move", required=false, defaultValue="h") 
		//binds the value of the query string parameter name into the name parameter of the greeting() method
		String name, Model model) {
		model.addAttribute("arg", name);
		return "gui";
 	}	
    
	@PostMapping( path = "/h" ) 
	public String doHalt( Model model ) {
		model.addAttribute("arg", "doing stop");
		return "gui";		
	}
	

    @ExceptionHandler 
    public ResponseEntity<String> handle(Exception ex) {
    	HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>(
        		"BaseController ERROR " + ex.getMessage(), responseHeaders, HttpStatus.CREATED);
    }

}

