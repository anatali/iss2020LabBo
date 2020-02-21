package it.unibo.iss.it.unibo.springbootIntro;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
 

/*
 * the fundamental difference between a web application and a REST API is that 
 * the response from a web application is generally view (HTML + CSS + JavaScript)  
 * because they are intended for human viewers while 
 * REST API just return data in form of JSON or XML 
 * because most of the REST clients are programs. 
 * This difference is also obvious in the @Controller and @RestController annotation.
 * Read more: https://javarevisited.blogspot.com/2017/08/difference-between-restcontroller-and-controller-annotations-spring-mvc-rest.html#ixzz6EZz3m5yT
 
 * The job of @Controller is to create a Map of model object and find a view 
 * but @RestController simply return the object and object data is directly written 
 * into HTTP response as JSON or XML.

 */
 
@Controller 
public class AController { 
    @Value("${spring.application.name}")
    String appName;
    
	@GetMapping("/") 		//shortcut for @RequestMapping(method = RequestMethod.GET)
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "welcome";
    } 
	
 
    @GetMapping("/model")  
    public String showModel(Model model) {
    	//Object currentCommand = model.getAttribute("curCmd");
    	model.addAttribute("curState", ApplModel.curState);
        //return String.format("AController currentCommand=%s", currentCommand );  
    	return "model";
    }
 
}

