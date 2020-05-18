package it.unibo.webspringflux.intro;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
 
@Controller 
public class BaseController { 
    @Value("${spring.application.name}")
    String appName;

/*
 * 1)      
 */    
  @GetMapping("/") 		 
  public String homePage(Model model) {
	  System.out.println("------------------- BaseController webflux homePage " + model  );
      model.addAttribute("arg", appName);
      return "welcome";
   } 
  
	@GetMapping("/greeting")
	public String greeting(
			@RequestParam(name="name", required=false, defaultValue="World") //binds the value of the query string parameter name into the name parameter of the greeting() method
			String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}
    
    @ExceptionHandler 
    public ResponseEntity<String> handle(Exception ex) {
    	HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>(
        		"BaseController ERROR " + ex.getMessage(), responseHeaders, HttpStatus.CREATED);
    }

}

