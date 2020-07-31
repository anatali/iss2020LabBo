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

	public void demoFP0() {
		System.out.println("demoFP0 ---------- ");
		Flux<String> flux1 = Flux.just("alpha", "bravo", "charlie");
		flux1	
			.map( String::toUpperCase )
  			.subscribe( System.out::println);
 	}

	public void demoFP1() {
		System.out.println("demoFP1 ---------- ");
		Flux<String> flux1 = Flux.just("alpha", "bravo", "charlie");
		flux1	
			.map( String::toUpperCase )
  			.flatMap( s -> Flux.fromArray( s.split("A")  ))
 			.subscribe( System.out::println);		 
	}
	
	public void demoFP2() {
		System.out.println("demoFP2 ---------- ");
		Flux<String> flux1 = Flux.just("alpha", "bravo", "charlie");
		flux1	
			.map( String::toUpperCase )
  			.flatMap( s -> Flux.fromArray( s.split("") ))
  			.groupBy(String::toString)							//https://projectreactor.io/docs/core/release/reference/
  			.sort( (o1,o2) -> o1.key().compareTo(o2.key() ))                        //UnicastGroupedFlux  			
  			.flatMap(Flux::collectList)
  			.subscribe( System.out::println);
 	}

	public void demoFP3() {
		System.out.println("demoFP3 ---------- ");
		Flux<String> flux1 = Flux.just("alpha", "bravo", "charlie");
		flux1	
			.map( String::toUpperCase )
  			.flatMap( s -> Flux.fromArray( s.split("") ))
  			.groupBy(String::toString)							//https://projectreactor.io/docs/core/release/reference/
  			.sort( (o1,o2) -> o1.key().compareTo(o2.key() ))                        //UnicastGroupedFlux  			
   			.flatMap( group -> Mono.just( group.key() ) )
 			.subscribe( System.out::println);		 
	}

	//From https://stackoverflow.com/questions/47924107/java-flux-groupedflux-count-print
	public void demoFP4() {
		System.out.println("demoFP4 ---------- ");
		Flux<String> flux1 = Flux.just("alpha", "bravo", "charlie");
		flux1	
			.map( String::toUpperCase )
  			.flatMap( s -> Flux.fromArray( s.split("") ))
  			.groupBy( String::toString )							//https://projectreactor.io/docs/core/release/reference/
  			.sort( (o1,o2) -> o1.key().compareTo(o2.key() ))                        //UnicastGroupedFlux  			
   			//.flatMap( group -> Mono.just( group.key() ).and( group.count() ))   
   			.flatMap(group -> Mono.zip(Mono.just(group.key()), group.count()))
   			.map(keyAndCount -> keyAndCount.getT1() + " => " + keyAndCount.getT2() + "; ")
//   			.map( g -> g.key() + " " + g.key().length()   )
     			.map( g ->  g  )
			.subscribe( System.out::println );		 
	}
	
	public void demoFP5() { 
		System.out.println("demoFP5 ---------- ");
		Flux<Integer> flux1 = Flux.just(1, 3, 5, 2, 4, 6, 11, 12, 13);
		   flux1.groupBy(i -> i % 2 == 0 ? "even" : "odd")
				.concatMap(g -> 
					   g.defaultIfEmpty(-1)   //if empty groups, show them
						.map(String::valueOf) //map to string
 						.startWith(g.key())    //start with the group's key
			     )
//				.flatMap(g -> 
//				   g.defaultIfEmpty(-1)   //if empty groups, show them
//					.map(String::valueOf) //map to string
//					.startWith(g.key())    //start with the group's key
//		     )
				.subscribe( System.out::println );	
 	}
	
	public static void main( String[] args) {
		ReactorDemo appl = new ReactorDemo();
//		appl.demoMonoPublisher();
//		appl.demoFluxPublisher();
//		appl.demoFP0();
//		appl.demoFP1();
//		appl.demoFP2();
//		appl.demoFP3();
 		appl.demoFP4();
		appl.demoFP5();
	}
}
