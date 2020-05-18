package it.unibo.qak.robotweb;
//See https://spring.io/guides/gs/serving-web-content/

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Controller 
public class BaseController { 
    //@Value("${spring.application.name}")
    @Value("${spring.application.name} | robotControl")
    String appName;

/*
 * 1)      
 */
    
  @RequestMapping("/") 		//shortcut for @RequestMapping(method = RequestMethod.GET)
  public String homePage(Model model) {
	  System.out.println("------------------- BaseController homePage " + model + " appName=" + appName);
      model.addAttribute("arg", appName);
      //return "welcome";
      return "index.html";
  } 
    
/*
 * 2)     
 */
	@RequestMapping("/cmd21")
	public String getAndPost ( Model model, ServerWebExchange exchange ) {
		System.out.println("getPost21 " + exchange.getRequest().getRemoteAddress() );
		model.addAttribute("arg", ""+exchange.getRequest().getRemoteAddress());		
		return  "index" ;  
	}
 

/*
* 3)     
*/	
	//@RequestMapping("/get/and/post/form-data-www-urleconded")
	@RequestMapping("/cmd21flux")
	public Mono<String> getAndPost ( ServerWebExchange exchange ) {
		//return  "Got: param1="+param1;  
		return  getFormData(exchange); //"Got: param1="+param1 ;
 	}
	
	private Mono<String> getFormData(ServerWebExchange serverWebExchange) {
	    return serverWebExchange.getFormData()
	        .flatMap(formData -> {
	            MultiValueMap<String, String> formDataResponse = new LinkedMultiValueMap<>();
	            formDataResponse.addAll(formData);
	            System.out.println("getFormData " + formDataResponse);
	            return Mono.just("cmd21 done "+ formDataResponse.getFirst("move")  + " " + formDataResponse.get("move")); //cmd21 done r [r]
	        });
	}
  
  
  
  
  
  @GetMapping("/test1")
    @ResponseBody
    public String test1() {
          return String.format("BaseController test1 "  );      
    }     
    
    @GetMapping("/test2")
    @ResponseBody
    public Publisher<String> handler2() {
        return Mono.just("BaseController test2 "); 
    }     
    
    
    @RequestMapping(value = "/testURLENCODED", method = RequestMethod.POST,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public @ResponseBody String testURLENCODED( String move ) {
        return  "testURLENCODED move="+move; 
    }    
    

   
	@PostMapping( path = "/testTEXT" ) 
	public String testTEXT(@RequestParam String move) {//@RequestParam Required String parameter 'move' is not present
	  return String.format("BaseController testTEXT "  );  //no @RequestParam => Could not resolve view
	}	
	
    @GetMapping("/testNORMAL")
    @ResponseBody
    public String testNORMAL() {
          return String.format("BaseController testNORMAL "  );      
    }     
	
	
      
    @ExceptionHandler 
    public ResponseEntity<String> handle(Exception ex) {
    	HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>(
        		"BaseController ERROR " + ex.getMessage(), responseHeaders, HttpStatus.CREATED);
    }
 
}

