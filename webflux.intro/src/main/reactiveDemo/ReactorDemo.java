package reactiveDemo;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import reactor.core.Disposable;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class ReactorDemo {
	
	public void demoMono() {
		System.out.println("demoMono ---------- ");
		Mono<String> mono1 = Mono.empty();
		Mono<String> mono2 = Mono.just("Bob");
		mono1.subscribe( item -> System.out.println("demoMono: " + item) );
		mono2.subscribe( item -> System.out.println("demoMono: " + item) );
	}
	public void demoFlux() {
		int n = 0;
		Flux<String> flux1 = Flux.just("A"+ n++, "B" + n++, "C"+ n++);
		Flux<String> flux2 = Flux.fromArray(new String[]{"A"+ n++, "B" + n++, "C"+ n++});
		Flux<String> flux3 = Flux.fromIterable(Arrays.asList("A"+ n++, "B" + n++, "C"+ n++));		
		System.out.println("demoFlux ---------- ");
		flux1.subscribe( item -> System.out.println("demoFlux - 1: " + item) );
		flux1.subscribe( item -> System.out.println("demoFlux - 1: " + item) );
 		flux2.subscribe( item -> System.out.println("demoFlux - 2: " + item) );
 		flux2.subscribe( item -> System.out.println("demoFlux - 2: " + item) );
 		flux3.subscribe( item -> System.out.println("demoFlux - 3: " + item) );		
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
	//Example 11. Example of state-based generate
	public void demoFluxGen0() {
		System.out.println("demoFluxGen0 ---------- ");
		Flux<String> flux = Flux.generate(
			    () -> 0, 			// initial state value
			    (state, sink) -> {
			      sink.next("3 x " + state + " = " + 3*state); 
			      if (state == 10) sink.complete(); 
			      return state + 1; 	//return a new state that we use in the next invocation
			    });	
		flux.subscribe( item -> System.out.println("demoFluxGen0 - 1: " + item) );
		flux.subscribe( item -> System.out.println("demoFluxGen0 - 2: " + item) );
	}
	
	//Example 12. Mutable state variant
	public void demoFluxGen1() {
		System.out.println("demoFluxGen1 ---------- ");
		Flux<String> flux = Flux.generate(
		    AtomicLong::new, 		//generate a mutable object as the state
		    (state, sink) -> {
		      long i = state.getAndIncrement(); 	//mutate the state 
		      sink.next("3 x " + i + " = " + 3*i);
		      if (i == 5) sink.complete();
		      return state; 		//return the same instance as the new state
	     });
		  flux.subscribe( item -> System.out.println("demoFluxGen1 - 1: " + item) );
		  flux.subscribe( item -> System.out.println("demoFluxGen1 - 2: " + item) );
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
			      if (i == 5) sink.complete();
			      return state; 
			    }, (state) -> System.out.println("state: " + state)); 		
		flux.subscribe( item -> System.out.println("demoFluxGen2 - 1: " + item) );
		flux.subscribe( item -> System.out.println("demoFluxGen2 - 2: " + item) );
	}
	
	//create is a more advanced form of programmatic creation of a Flux which is suitable for  
	//multiple emissions per round, even from multiple threads.
	public void demoCreate0() {  
		Flux<String> bridge = Flux.create(emitter -> {	//Consumer<? super FluxSink<String>> emitter
			for (int i = 0; i < 3; i++) {
				emitter.next(String.valueOf(i));
			}
		});
		bridge.subscribe(item -> System.out.println("demoFluxCreate0 - 1: " + item));
		bridge.subscribe(item -> System.out.println("demoFluxCreate0 - 2: " + item));
	}

	public void demoDisposable() {
//	int n = 0;
//	Disposable disp = Flux.fromIterable( Arrays.asList("A"+ n++, "B" + n++, "C"+ n++) );
//		disp.flatMap { customer -> client.call(customer) }
//			  .subscribe();	
	}
	
	/*sez. 4.5
		In Reactor, the execution model and where the execution happens is determined by the Scheduler that is used. 
		A Scheduler has scheduling responsibilities similar to an ExecutorService, but having a dedicated abstraction 
		lets it do more, notably acting as a clock and enabling a wider range of implementations 
		(virtual time for tests, trampolining or immediate scheduling, and so on).	  
		
		Reactor offers two means of switching the execution context (or Scheduler) in a reactive chain: publishOn and subscribeOn.
	 
		In Reactor, when you chain operators, you can wrap as many Flux and Mono implementations inside one another as you need. 
		Once you subscribe, a chain of Subscriber objects is created, backward (up the chain)  to the first publisher. 
		This is effectively hidden from you. 
		All you can see is the outer layer of Flux (or Mono) and Subscription, but these intermediate 
		operator-specific subscribers are where the real work happens.	 
		With that knowledge, we can have a closer look at the publishOn and subscribeOn operators. 
		------------------------------------------		
        In Reactor, there is no sense in wanting to cancel a Subscription before you've called subscribe() 
        (as it is that very method that creates the Subscription and propagates that signal up the chain to start the emission of data).
        
	    Note some operators will also cancel subscriptions on your behalf! 
	    That is the case for take(int) for instance, which will cancel upstream once enough items have been emitted.
	*/
	
	public void demoDuration() {
		System.out.println("demoDuration ---------- ");
		//Flux<Long> timer = Flux.interval( Duration.ofMillis(500 ) ) //enabled by Schedulers.parallel() sez. 4.5
		Scheduler disiScheduler = Schedulers.newSingle("disiScheduler");	//a per-call dedicated thread
		//Scheduler disiSameThreadScheduler = Schedulers.single( );
		//changes the Scheduler to a new instance similar to Schedulers.single()	//sez. 4.5
		Flux<Long> timer = Flux.interval( Duration.ofMillis(500 ), disiScheduler ) 
				.map( tick -> {if (tick <= 6) return tick; throw new RuntimeException("Aborted"); } );  //generate an error
 		
		// %%%%%%%%%%%%%%%%% Nothing happens until you subscribe %%%%%%%%%%%%%%%%%
		timer.take(3).subscribe( 
 				tick  -> System.out.println("subscriber0 tick= " + tick ),
 				error -> System.out.println("subscriber0 error= " + error ),
 				()    -> System.out.println("subscriber0 done "   )
 		);

		timer.subscribe( 
 				tick  -> System.out.println("subscriber1 tick= " + tick ),
 				error -> System.out.println("subscriber1 error= " + error ),
 				()    -> System.out.println("subscriber1 done "   )
 		);
		
		
		timer.subscribe( 
				new Subscriber<Long>() {
				    @Override
				    public void onSubscribe(Subscription s) {
				    	System.out.println("subscriber2 - onSubscribe Long.MAX_VALUE= " + Long.MAX_VALUE );
				    	s.request( 5 );		//0-4 only
 				    }
				 
				    @Override
				    public void onNext(Long v) {
				      System.out.println("subscriber2 - onNext: " + v );
				      //elements.add(integer);
				    }
				 
				    @Override
				    public void onError(Throwable t) {
				    	System.out.println("subscriber2 - onError " + t );				    	
				    }
				
				    @Override
				    public void onComplete() {
				    	System.out.println("subscriber2 - onComplete "  );
				    }

 				});
		
 		delay(6000);
 		disiScheduler.dispose();
 		System.out.println("demoDuration BYE "  ); 
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
	public void demoFluxPush0() {
		System.out.println("demoFluxPush0 ---------- ");
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
	
 

	//push is a middle ground between generate and create which is suitable for processing events from a single producer.
	public void demoPush0() {
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
		bridge.subscribe( item -> System.out.println("demoPush0 - 1: " + item));
		bridge.subscribe( item -> System.out.println("demoPush0 - 2: " + item));
	}
	
	
	public void demoCold0() {
		Flux<String> source = Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                .map(String::toUpperCase);
			source.subscribe(d -> System.out.println("Subscriber 1: "+d));
			source.subscribe(d -> System.out.println("Subscriber 2: "+d));		
	}
	
	//The process described by the operators on this Flux runs regardless of when subscriptions have been attached.
	public void demoHot0() {
		/*
		 * A direct Processor is a processor that can dispatch signals to zero or more Subscribers. 
		 */
		DirectProcessor<String> hotSource = DirectProcessor.create();		//sez. 4.7.3 it has the limitation of not handling backpressure		
		/*
		 * a DirectProcessor signals an IllegalStateException to its subscribers if you push N elements through it 
		 * but at least one of its subscribers has requested less than N.
		 */
		Flux<String> hotFlux = hotSource.map(String::toUpperCase);

		hotFlux.subscribe(d -> System.out.println("Subscriber 1: "+d));

		hotSource.onNext("blue");
		hotSource.onNext("green");

		hotFlux.subscribe(d -> System.out.println("Subscriber 2: "+d));

		hotSource.onNext("orange");
		hotSource.onNext("purple");
		hotSource.onComplete();		
	}

	/*
	 * Backpressure is the ability for the consumer to signal the producer that the rate of emission is too high. sez 3.3.5
	 *	A subscriber can work in unbounded mode and let the source push all the data at its fastest achievable rate 
	 *	or it can use the request mechanism to signal the source that it is ready to process at most n elements.	  
	 */
	public void demoHot1() {
		//Generates an exception
		int curVal = 0;
		/*
		 * A direct Processor is a processor that can dispatch signals to zero or more Subscribers. 
		 */
		DirectProcessor<Integer> hotSource = DirectProcessor.create();	//sez. 4.7.3 it has the limitation of not handling backpressure		
		/*
		 * a DirectProcessor signals an IllegalStateException to its subscribers if you push N elements through it 
		 * but at least one of its subscribers has requested less than N.
		 */
		Flux<Integer> hotFlux = hotSource 
				.map( v -> {if (v <= 8) return v; throw new RuntimeException("Aborted"); } );  //generate an error

		hotFlux.take(3).subscribe(
  				d  -> System.out.println("Full observer: "+d),
  				error -> System.out.println("Full observer error: "+ error),
  				() -> System.out.println("Full observer ENDS " )
  		);	 

   		for( int i= 0; i<5; i++ ) { 
  			delay(500); 
  			hotSource.onNext( curVal++ ); 
  		}
		
		hotFlux.subscribe(
				d -> System.out.println("Partial observer: "+d),
  				error -> System.out.println("Partial observer error: "+ error),
  				() -> System.out.println("Partial observer ENDS " )
		);

   		for( int i= 0; i<5; i++ ) { 
  			delay(500); 
  			hotSource.onNext( curVal++); 
  		}
		
		hotSource.onComplete();		
	}
	
	private void delay(int dt) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}
	
	public static void main( String[] args) {
		ReactorDemo appl = new ReactorDemo();
//		appl.demoMono();
//		appl.demoFlux();
		
// 		appl.demoFluxGen0();
//  	appl.demoFluxGen1();
// 		appl.demoFluxGen2();

		
//		appl.demoFP0();
//		appl.demoFP1();
//		appl.demoFP2();
//		appl.demoFP3();
// 		appl.demoFP4();
//		appl.demoFP5();
		
//		appl.demoFlux1();
		
		
		
// 		appl.demoCreate0();
		
// 		appl.demoPush0();
		
//		appl.demoCold0();
//		appl.demoHot0();
		appl.demoHot1();
		
//		appl.demoDuration();
	}
}
