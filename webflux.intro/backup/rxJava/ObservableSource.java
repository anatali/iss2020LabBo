package rxJava;
/*
import java.util.concurrent.CountDownLatch;
import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class ObservableSource {
private CountDownLatch latch;

	public Observable<String> operation() {

		return Observable.<String> create(s -> {
			System.out.println("Start: Executing slow task in ObservableSource");
			for(int i=1; i<=3;i++){
				Utils.delay(500);
				s.onNext("data " + i);
			}
			println("End: Executing slow task in ObservableSource");
			s.onComplete();
		}).subscribeOn(Schedulers.computation());
	}

	public void doJob1() {

		Observable<String> op1 = operation();

		op1.subscribe(s -> println("From Subscriber A:" + s), e -> println("" + e.getMessage()),
				() -> println("From Subscriber A1:") );

		op1.subscribe(s -> println("From Subscriber B:" + s), e -> println("" + e.getMessage()),
				() -> println("From Subscriber B1:"));
	}

	public void doJob2() {
		Observable<String> op1 = operation();
		ConnectableObservable<String> connObs = op1.publish();
		println("connObs="+connObs);
		connObs.subscribe(
				s -> println("Hot A:" + s), 
				e -> println("Hot A e:" + e.getMessage()),
				() -> println("Hot A1 ends"));

		connObs.connect();
		Utils.delay(600);
		connObs.subscribe(
				s -> println("Hot B:" + s), 
				e -> println("Hot B e:" + e.getMessage()),
				() -> println("Hot B1 ends"));
	}
	
	public void doJob3() throws InterruptedException {
		latch =  new CountDownLatch(2);
		Observable<String> op1 = operation();
		PublishSubject<String> publishSubject = PublishSubject.create();
 		op1.subscribe(publishSubject);
		publishSubject.subscribe(
				s -> println("Hot A:" + s), 
				e -> println("Hot A e:" + e.getMessage()),
				() -> latch.countDown()  );
		Utils.delay(600);
		publishSubject.subscribe(
				s -> println("Hot B:" + s), 
				e -> println("Hot B e:" + e.getMessage()),
				() -> latch.countDown() );
		
		latch.await();
	}
	

	protected void println(String msg) {
//		System.out.println(msg);
		Utils.log(msg);
	}

 
//MAIN
 
	public static void main(String[] args) throws InterruptedException {
		new ObservableSource().doJob3();
//		Utils.delay(5000);
	}
}
	*/

