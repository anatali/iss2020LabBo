package reactiveDemo;

import java.util.Arrays;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactorDemo {
	
	public void demoFluxPublisher() {
		Flux<String> flux1 = Flux.just("A", "B", "C");
		Flux<String> flux2 = Flux.fromArray(new String[]{"A", "B", "C"});
		Flux<String> flux3 = Flux.fromIterable(Arrays.asList("A", "B", "C"));
		
		System.out.println("demoFluxPublisher ---------- ");
		flux1.subscribe( System.out::println);
		
	}
	public void demoMonoPublisher() {
		System.out.println("demoMonoPublisher ---------- ");
		Mono<String> mono1 = Mono.empty();
		Mono<String> mono2 = Mono.just("Bob");

		mono1.subscribe( System.out::println);
		mono2.subscribe( System.out::println);

	}
	
	public void demoFP() {
		System.out.println("demoFP ---------- ");
		Flux<String> flux1 = Flux.just("alpha", "bravo", "charlie");
		flux1	
			.map( String::toUpperCase )
  			.flatMap( s -> Flux.fromArray( s.split("") ))
  			.groupBy(String::toString)												//UnicastGroupedFlux
  			.sort( (o1,o2) -> o1.key().compareTo(o2.key() ))                        //UnicastGroupedFlux  			
// 			.flatMap( group -> Mono.just( group.key() ).and( group.count()) )
 			.flatMap(Flux::collectList)                                             //By AN
// 			.map( keyAndCount -> keyAndCount.getT1() + " => " + keyAndCount.getT2()) 
			.subscribe( System.out::println);
		 
	}

	
	public static void main( String[] args) {
		ReactorDemo appl = new ReactorDemo();
//		appl.demoMonoPublisher();
//		appl.demoFluxPublisher();
		appl.demoFP();
	}
}
