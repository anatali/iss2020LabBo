package it.unibo.Clients;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.howtodoinjava.demo.model.Employee;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

public class Client1 {
/*
	private WebClient webClient = WebClient.create("http://localhost:8082");
 	
	public Mono<Employee> create(Employee empl)
	{
		System.out.println("create");
	    return webClient.post()
	        .uri("/employees")
	        .body(Mono.just(empl), Employee.class)
	        .retrieve()
	        .bodyToMono(Employee.class);
	}
	
	
	public Flux<Employee> findAll() 
	{
	    return webClient.get()
	        .uri("/employees")
	        .retrieve()
	        .bodyToFlux(Employee.class);
	}
	
	public Mono<Employee> findById(Integer id) 
	{
	    return webClient.get()
	        .uri("/employees/" + id)
	        .retrieve()
	        /*.onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus),
	                clientResponse -> Mono.empty())*/
	        .bodyToMono(Employee.class);
	}
	
//	public Mono<Employee> update(Employee e) 
//	{
//	    return webClient.put()
//	        .uri("/employees/" + e.getId())
//	        .body(Mono.just(e), Employee.class)
//	        .retrieve()
//	        .bodyToMono(Employee.class);
//	}
	
	@Bean
	public WebClient getWebClient()
	{
	    HttpClient httpClient = HttpClient.create()
	            .tcpConfiguration(client ->
	                    client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
	                    .doOnConnected(conn -> conn
	                            .addHandlerLast(new ReadTimeoutHandler(10))
	                            .addHandlerLast(new WriteTimeoutHandler(10))));
	     
	    ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);     
	 
	    return WebClient.builder()
	            .baseUrl("http://localhost:3000")
	            .clientConnector(connector)
	            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
	            .build();
	}	
	
	
	
	public void buildAnHttpRequest() {  
	    WebClient webClient = WebClient.create("http://localhost:8082");

	    Mono<String> result = webClient.get()
	        .uri(uriBuilder -> uriBuilder
	            .path("/22")
	            .queryParam("name", "Koding")
	            .build())
	        .retrieve()
	        .bodyToMono(String.class);
	    
	    System.out.println( result.block() );
	    
//	    assertThat(result.block())
//	        .isEqualTo("Hello, Koding!");
	}	
	
	/*
		The retrieve() method directly performs the HTTP request and retrieve the response body. 
		The exchange() method returns ClientResponse having the response status and headers. 
		We can get the response body from ClientResponse instance.
	 */
	
//	public void xxx() {
//		Mono<Employee> createdEmployee = webClient.post()
//		        .uri("/employees")
//		        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//		        .body(Mono.just(empl), Employee.class)
//		        .retrieve()
//		        .bodyToMono(Employee.class);		
//	}
// 	
	public static void main(String[] args) {
		Client1 c = new Client1();
 		Employee empl     = new Employee();
 
//	    Mono<Employee> e1 = c.create( empl );
//	    System.out.println( e1.toString()  );
	    
	    c.buildAnHttpRequest();
	}
}


/*
WebClient webClient2 = WebClient.builder()
.baseUrl("http://localhost:3000")
.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
.build();
*/