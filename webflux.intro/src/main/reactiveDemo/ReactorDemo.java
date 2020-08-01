package reactiveDemo;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

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
	//From https://stackoverflow.com/questions/41970036/project-reactor-how-to-create-a-sliding-window-flux-from-a-java-8-stream
	public void demoFlux0() { 
		System.out.println("demoFlux0 ---------- ");
		Flux<Integer> primary = Flux.fromStream(IntStream.range(0, 10).boxed());
		primary.flatMap(num -> Flux.just(num, num))
		    .skip(1)
		    .buffer(2)
		    .filter(list -> list.size() == 2)
		    .map(list -> Arrays.toString(list.toArray()))
		    .subscribe(System.out::println);
		}
//	Java 8 Streams do not permit reuse. 
//	This creates a puzzle about how to reuse a stream when creating a sliding window flux 
//	to calculate a relationship like x(i)*x(i-1).	 SEE SlidingWindow
//	-------- BUT: Solution using Reactor Core 3
//	The magic is the buffer(2, 1) part: here maxSize is 2, and skip is 1. 
//	Since maxSize is greater than skip, this creates 
//	overlapping buffers (that is, sliding windows) over the flux, and emits each buffer as a List. 
//	The skipLast(1) is needed, as the last buffer will be a single element (of 10), this needs to be skipped.
	public void demoFlux1() { 
		System.out.println("demoFlux1 ---------- ");
		Flux.fromStream(IntStream.rangeClosed(1, 10).boxed())
	        .buffer(2, 1)
	        .skipLast(1)
	        .map(t -> t.stream().reduce((a, b)-> a*b))
	        .subscribe(System.out::println);
	}
	
	//From https://projectreactor.io/docs/core/release/reference/#producing
	public void demoFluxGen0() {
		System.out.println("demoFluxGen0 ---------- ");
		Flux<String> flux = Flux.generate(
			    () -> 0, 
			    (state, sink) -> {
			      sink.next("3 x " + state + " = " + 3*state); 
			      if (state == 10) sink.complete(); 
			      return state + 1; 	//return a new state that we use in the next invocation
			    });	
		flux.subscribe(System.out::println);
	}
	
	public void demoFluxGen1() {
		System.out.println("demoFluxGen1 ---------- ");
		Flux<String> flux = Flux.generate(
		    AtomicLong::new, 		//generate a mutable object as the state
		    (state, sink) -> {
		      long i = state.getAndIncrement(); 	//We mutate the state here
		      sink.next("3 x " + i + " = " + 3*i);
		      if (i == 10) sink.complete();
		      return state; 		//We return the same instance as the new state
	     });
		flux.subscribe(System.out::println);
	}
	//the generate method that includes a Consumer
	//In the case of the state containing a database connection or other resource that needs to be handled 
	//at the end of the process, the Consumer lambda could close the connection 
	//or otherwise handle any tasks that should be done at the end of the process.
	public void demoFluxGen2() {
		System.out.println("demoFluxGen2 ---------- ");
 		Flux<String> flux = 
				Flux.generate(
			    AtomicLong::new,
			      (state, sink) -> { 
			      long i = state.getAndIncrement(); 
			      sink.next("3 x " + i + " = " + 3*i);
			      if (i == 10) sink.complete();
			      return state; 
			    }, (state) -> System.out.println("state: " + state)); 		
		flux.subscribe(System.out::println);
	}
	
	//create is a more advanced form of programmatic creation of a Flux which is suitable for  
	//multiple emissions per round, even from multiple threads.
	public void demoFluxCreate0() {
		Flux<String> bridge = Flux.create(emitter -> {
			for (int i = 0; i < 3; i++) {
				emitter.next(String.valueOf(i));
			}
		});
		bridge.subscribe(System.out::println);
	}
	
	/*
	public void demoFluxCreate1() {
//		CustomSpringEventListener myEventProcessor = new CustomSpringEventListener();
		Flux<String> bridge = Flux.create(sink -> {
		    myEventProcessor.register( //All of this is done asynchronously whenever the myEventProcessor executes
//		      new MyEventListener<String>() { 
 		        public void onDataChunk(List<String> chunk) {
		          for(String s : chunk) {
		            sink.next(s); 
		          }
		        }

		        public void processComplete() {
		            sink.complete(); 
		        }
		    });
		});		
		bridge.subscribe(System.out::println);
	}
	*/
	
	/*
	//push is a middle ground between generate and create which is suitable for processing events from a single producer.
	public void demoFluxPush0() {
		System.out.println("demoFluxGen2 ---------- ");
		Flux<String> bridge = Flux.push(sink -> {
		    myEventProcessor.register(	//	All of this is done asynchronously whenever the myEventProcessor executes
		      new SingleThreadEventListener<String>() { 

		        public void onDataChunk(List<String> chunk) {
		          for(String s : chunk) {
		            sink.next(s); 
		          }
		        }

		        public void processComplete() {
		            sink.complete(); 
		        }

		        public void processError(Throwable e) {
		            sink.error(e); 
		        }
		    });
		});		
		bridge.subscribe(System.out::println);
	}
		*/
	
 

	public void demoFluxPush0() {
		System.out.println("demoFluxPush0 ---------- ");
		Flux<String> bridge = Flux.push(sink -> {
			new Thread() {
				int n = 0;
				public void run() {
					while( n < 5 ) {
						sink.next("a" + n++);
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
	 						e.printStackTrace();
						}
					}//while
					sink.complete();
				}
			}.start();
			 
 		});		
		bridge.subscribe(System.out::println);
	}
		
	public static void main( String[] args) {
		ReactorDemo appl = new ReactorDemo();
//		appl.demoMonoPublisher();
//		appl.demoFluxPublisher();
//		appl.demoFP0();
//		appl.demoFP1();
//		appl.demoFP2();
//		appl.demoFP3();
// 		appl.demoFP4();
//		appl.demoFP5();
		
//		appl.demoFlux1();
		
//		appl.demoFluxGen0();
// 		appl.demoFluxGen1();
//		appl.demoFluxGen2();
		
		
//		appl.demoFluxCreate0();
		
		appl.demoFluxPush0();
	}
}
