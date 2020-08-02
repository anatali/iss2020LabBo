package reactiveDemo;
//From https://www.e4developer.com/2018/04/28/springs-webflux-reactor-parallelism-and-backpressure/
import org.reactivestreams.Subscription;

import reactor.core.publisher.BaseSubscriber;

public class BackpressureReadySubscriber<T> extends BaseSubscriber<T> {
	 
    public void hookOnSubscribe(Subscription subscription) {
        //requested the first item on subscribe
        request(1);
    }
 
    public void hookOnNext(T value) {
        //process value
        //processing...
        //once processed, request a next one
        //you can implement specific logic to slow down processing here
        request(1);
    }
    
    /*
BackpressureReadySubscriber<String> bSubcriber = new BackpressureReadySubscriber<>();
Flux<String> source = stringbasedSource();
source.subscribe(bSubcriber);

     */
}
