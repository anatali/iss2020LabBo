package it.unibo.qak.robotweb;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import reactor.core.publisher.Mono;

@Controller 
public class AController { 
    @Value("${spring.application.name}")
    String appName;
     
    @GetMapping("/test")
    @ResponseBody
    public Publisher<String> handler() {
        return Mono.just("AController: Hello world from handler!");
    }     
    
    @ExceptionHandler 
    public ResponseEntity<String> handle(Exception ex) {
    	HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<String>(
        		"AController ERROR " + ex.getMessage(), responseHeaders, HttpStatus.CREATED);
    }
 
}

