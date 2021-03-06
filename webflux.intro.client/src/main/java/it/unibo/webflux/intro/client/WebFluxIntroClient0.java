package it.unibo.webflux.intro.client;

import java.time.Duration;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//https://howtodoinjava.com/spring-webflux/webclient-get-post-example/
public class WebFluxIntroClient0 {
	
	
	private Mono<String> callForUri( WebClient webClient, String uripath ){
		Mono<String> result = webClient.get()
		        .uri( uripath )
		        .retrieve()
		        /*.onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus),
		                clientResponse -> Mono.empty())*/
		        .bodyToMono(String.class);
		return result;
	}
	
	public void callForHomePage() {
		WebClient webClient = WebClient.create("http://localhost:8081");
		Mono<String> result = callForUri(webClient, "/" );
		System.out.println(" %%%%%%%%%%%% callForHomePage");
		String outS = result.block();
		System.out.println(outS);
	}
	
	public void callFoString() {
		WebClient webClient = WebClient.create("http://localhost:8082");
		Mono<String> result = callForUri(webClient, "/api" );
		System.out.println(" %%%%%%%%%%%% callFoString");
		String outS = result.block();
		System.out.println("callForData answer= "+outS);
	}
	
	//
	public void callForFlux() {
		WebClient webClient = WebClient.create("http://localhost:8082");
        Flux<Long> result = webClient.get()
				.uri("/api/getfluxoflong")   
                .retrieve()
                .bodyToFlux(Long.class);
        System.out.println("WebClientExample | callForFlux response " + result);
        result.subscribe(  
        		item  -> System.out.println("item=  " + item),
        		error -> System.out.println("error= " + error ),
        		()    -> System.out.println("done " )   
        );
        Long last = result.blockLast( Duration.ofMillis(10000));
        System.out.println("WebClientExample | last= " + last); 
	}	

	public void callForFluxString ( String uriStr ) {
		WebClient webClient = WebClient.create("http://localhost:8082");
//      Mono<ResponseEntity<List<String>>> result1 = webClient.get()
//		.uri( uriStr )   
//        .retrieve()
//        .toEntityList(String.class);
       Flux<String> result = webClient.get()
				.uri( uriStr )   
                .retrieve()
                .bodyToFlux(String.class);
//         System.out.println("WebClientExample | callForFluxString response " + result);
        result.subscribe(  
        		item  -> System.out.println("item=  " + item),
        		error -> System.out.println("error= " + error ),
        		()    -> System.out.println("done " + uriStr )   
        );
        String last = result.blockLast( Duration.ofMillis(20000));
        System.out.println("WebClientExample | last= " + last);         
        //System.out.println("WebClientExample | result1= " + result1.block());
 	}	

    public static void main(String[] args)   {
    	
    	WebFluxIntroClient0 appl = new WebFluxIntroClient0();
    	appl.callForHomePage();
	
     	 //appl.callFoString( );
//     	 appl.callForFlux();
//    	 appl.callForFluxString("/api/fluxstring");
//    	 appl.callForFluxString("/api/getflux");
    	      
    }
}