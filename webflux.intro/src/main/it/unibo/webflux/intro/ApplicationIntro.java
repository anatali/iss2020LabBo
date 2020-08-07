package it.unibo.webflux.intro;
 
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
//import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;



//@SpringBootApplication
//@EnableWebFlux		//REDUNDANT
public class ApplicationIntro {

	public static void main(String[] args) {
 		//System.setProperty("spring.devtools.restart.enabled", "false"); //By AN: to avoid SilentExitException in Eclipse DEBUG
		SpringApplication.run(ApplicationIntro.class, args);
	}

//From Spring Boot WebFlux WebSocket Example	
/*
 * (1) At the center of the application, we will have a WebSocketHandler which will handle WebSocket messages 
 * and lifecycle events. The given EchoHandler will receive a message and return it prefixed with “RECEIVED ON SERVER ::”.
 * 
 * (2) WebSocketHandler needs to be mapped to a URL using the SimpleUrlHandlerMapping. 
 * 
 * (3) Then we need a WebSocketHandlerAdapter which will invoke the WebSocketHandler.
 * 
 * (4) Finally, to let the WebSocketHandlerAdapter understand the incoming reactive runtime request, 
 * we need to configure a WebSocketService with the ReactorNettyRequestUpgradeStrategy 
 * (as we are using default netty server).	
 */
	@Bean
	public EchoHandler echoHandler() {				//(1) implements WebSocketHandler 
		return new EchoHandler();
	}
	@Bean
	public HotSourceDataEmitter dataemitter() {				//(1) implements WebSocketHandler 
		return new HotSourceDataEmitter();
	}

	@Bean
	public HandlerMapping handlerMapping() {
		Map<String, WebSocketHandler> map = new HashMap<>();
		map.put("/echo", echoHandler());
		map.put("/data", dataemitter());
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();		//(2)
		mapping.setUrlMap(map);
		mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return mapping;
	}
 
	@Bean
	public WebSocketHandlerAdapter handlerAdapter() {				//(3)
		return new WebSocketHandlerAdapter(webSocketService());
	}

	@Bean
	public WebSocketService webSocketService() {		//(4)
		return new HandshakeWebSocketService(new ReactorNettyRequestUpgradeStrategy());
	}
	
	 
//	 @Bean
//	    public SecurityWebFilterChain functionalValidationsSpringSecurityFilterChain(ServerHttpSecurity http) {
//	        http.authorizeExchange()
//	            .anyExchange()
//	            .permitAll();
//	        http.csrf().disable();
//	        return http.build();
//	    }	
	     
}
