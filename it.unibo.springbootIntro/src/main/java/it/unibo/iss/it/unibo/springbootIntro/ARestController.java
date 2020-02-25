package it.unibo.iss.it.unibo.springbootIntro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.unibo.robot.robotAdapter;
import itunibo.robotMbot.SerialPortConnSupport;
import itunibo.robotMbot.mbotSupport;
import itunibo.robotMock.mockrobotSupport;

/*
 * the fundamental difference between a web application and a REST API is that 
 * the response from a web application is generally view (HTML + CSS + JavaScript)  
 * because they are intended for human viewers while REST API just return data in form of JSON or XML 
 * because most of the REST clients are programs. 
 * This difference is also obvious in the @Controller and @RestController annotation.
 * Read more: https://javarevisited.blogspot.com/2017/08/difference-between-restcontroller-and-controller-annotations-spring-mvc-rest.html#ixzz6EZz3m5yT
 
 * The job of @Controller is to create a Map of model object and find a view 
 * but @RestController simply return the object and object data is directly written 
 * into HTTP response as JSON or XML.

 */
@RestController  //combination of @Controller and @ResponseBody

public class ARestController { 
	private robotAdapter support = null;
	private ApplModel    model   = null;
	
	@Autowired
	public ARestController( RobotSupport support, ApplModel model ) {
  		//CoapSupport.createConnection("192.168.1.8", "8018", "basicrobot");	
		this.support = support;
		this.model   = model;
		support.create();	//TODO: do it as construction
 	}
//  	@PostMapping("/w")  
//  	public String doCmd() {	//@RequestBody String cmd
//    	ApplModel.curState = "executing w => " ;
//     	mockrobotSupport.INSTANCE.move("w");
//     	//mbotSupport.INSTANCE.move("w");
//  		return String.format("ARestController doCmd %s",  "w" );
//  	}
  	@PostMapping("/cmd")  
  	public String doCmd(@RequestBody String move) {	 //move=w
     	String cmd = move.split("=")[1];
    	support.move(cmd);
    	updateRobotModel( cmd );
   		return String.format("ARestController doCmd %s",  cmd );
  	}
  	
    @GetMapping("/model")  
    public String handleCmd(@RequestParam(value = "v", defaultValue = "h") String robotcmd ) {
         return String.format("ARestController model rep=%s", model.getRep() );  
     }
 
    @GetMapping("/robotstate")  
     public String showState(  ) {
      	return model.getRep();
    }

    private void updateRobotModel( String move) {
    	model.update( "robot is executing "+ move  );   	
    }
 
}

