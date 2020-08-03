package reactiveDemo;
//From https://www.e4developer.com/2018/04/28/springs-webflux-reactor-parallelism-and-backpressure/
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

public class CustomSubscriber<T> extends BaseSubscriber<T> { //sez. 4.3.3  
//https://projectreactor.io/docs/core/release/api/reactor/core/publisher/BaseSubscriber.html
private String name="";
private int n = 0;	 

	public CustomSubscriber( String name ){
		this.name = name;
	}
    public void hookOnSubscribe(Subscription subscription) {
        //requested the first item on subscribe
    	System.out.println(name+ " | hookOnSubscribe n=" + n);
        request(1);
    } 
    
    public void hookOnNext(T value) {
        //process value
    	n++;
        //once processed, request a next one
        //you can implement specific logic to slow down processing here
    	if( n > 2 ) {
        	delay(1500);
        	System.out.println(name + " | hookOnNext n=" + n + " delayed value=" + value);	   
    	}else System.out.println(name + " | hookOnNext n=" + n + " value=" + value);		  
        request(1);
    }
    
    public void hookOnComplete() {
    	System.out.println(name + " | hookOnComplete n=" + n  );		         	
    }
    
	private void delay(int dt) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}
    
    /*
BackpressureReadySubscriber<String> bSubcriber = new BackpressureReadySubscriber<>();
Flux<String> source = stringbasedSource();
source.subscribe(bSubcriber);

     */
}
