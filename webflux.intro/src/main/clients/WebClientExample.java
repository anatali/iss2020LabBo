package clients;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
/*
 * From https://www.baeldung.com/spring-log-webclient-calls
 * Behind the scenes, WebClient calls an HTTP client. 
 * Reactor Netty is the default and reactive HttpClient of Jetty is also supported. 
 * Moreover, it's possible to plug other implementations of HTTP client by setting up a ClientConnector for WebClient.
 */
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import reactor.core.publisher.Mono;

public class WebClientExample {
	
	public static ExchangeFilterFunction logRequest() {
	    return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
//	        if (log.isDebugEnabled()) {
//	            StringBuilder sb = new StringBuilder("Request: \n");
//	            //append clientRequest method and url
//	            clientRequest
//	              .headers()
//	              .forEach((name, values) -> values.forEach(value ->  "a" ));  /* append header key/value */
//	            log.debug( sb.toString() );
//	        }
//	        return Mono.just(clientRequest);
	    	return Mono.just( clientRequest );
	    });
	}
	
	public static ExchangeFilterFunction logResponse() {
	    return ExchangeFilterFunction.ofResponseProcessor( clienResponse -> {
	    	return Mono.just( clienResponse );
	    });
    }
	
//	public static JsonNode postToTodoAPI() {
//	    return this.defaultWebClient
//	    	      .post()
//	    	      .uri("/todos")
//	    	      .body(BodyInserters.fromValue("{ \"title\": \"foo\", \"body\": \"bar\", \"userId\": \"1\"}"))
//	    	      .retrieve()
//	    	      .bodyToMono(JsonNode.class)
//	    	      .block();
//	    	  }
    public static void main(String[] args) throws InterruptedException {
        //WebClient webClient = WebClient.create("http://localhost:8081");
    	
    	WebClient webClient = WebClient
	    	  .builder()
	    	  .filters(exchangeFilterFunctions -> {
	    	      exchangeFilterFunctions.add(logRequest());
	    	      exchangeFilterFunctions.add(logResponse());
	    	  })
	    	  .build();
    	 
        Mono<String> result = webClient.get()
        						.uri("http://localhost:8081")
                                .retrieve()
                                .bodyToMono(String.class);
        String response = result.block();
        System.out.println("WebClientExample2 handle response");
        System.out.println(response);
    }
}