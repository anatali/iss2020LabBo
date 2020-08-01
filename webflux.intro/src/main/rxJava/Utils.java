package rxJava;
/*
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

public class Utils {
	public static long start = System.currentTimeMillis();
	
	public static void log( Object msg ){
		long now = System.currentTimeMillis();
		System.out.println(
				now -start +"\t|"+
				Thread.currentThread().getName()+"\t|"+
				msg);
	}
	public static Consumer<String> logConsumer(String logo){
		return new Consumer<String>() {
			@Override
			public void accept(String s) {
				log(logo + s);
			}
		};		
	}
	public static Consumer<Throwable> logThrowable(String logo){
		return new Consumer<Throwable>() {
			@Override
			public void accept(Throwable t) throws Exception {
				log(logo + t);
			}
 		};		
	}
	public static Action logComplete (String logo){
		return new Action() {
			@Override
			public void run() throws Exception {
				log(logo );
 			}
  		};		
	}	
 
	public static Observer<String> logObserver(String logo){
		return new Observer<String>(){
			 
//			 * onSubscribe (not present in RxJava1) gets the Disposable as a parameter which  
//			 * can be used for disposing the connection between the Observable and the Observer itself 
//			 * as well as checking whether we're already disposed or not.
			 
			@Override
			public void onSubscribe(Disposable d) {
				log(logo + "subscribes " + d  );
			}
			@Override
			public void onNext(String s) {
				log(logo + s);
			}
			@Override
			public void onError(Throwable e) {
 				e.printStackTrace();
			}
			@Override
			public void onComplete() {
				log(logo + "completed");
			}			
		};
	}
	
 
	public static DisposableObserver<String> logDisposableObserver(String logo) {
		return new DisposableObserver<String>() {
			@Override
			public void onNext(String s) {
				log(logo + s);
			}
			@Override
			public void onError(Throwable e) {
					e.printStackTrace();
			}
			@Override
			public void onComplete() {
				log(logo + "completed");
			}			
		};		
	}
	
 
	public static ObservableOnSubscribe<Integer> source123 = new ObservableOnSubscribe<Integer>(){
		@Override
		public void subscribe(ObservableEmitter<Integer> e) throws Exception {
			e.onNext(1);
			e.onNext(2);
			e.onNext(3);
			e.onComplete();
	 	}	
	};
 
	public static ObservableOnSubscribe<String> sourceABC = new ObservableOnSubscribe<String>(){
		@Override
		public void subscribe(ObservableEmitter<String> e) throws Exception {
			delay(500);
			e.onNext("A");
			delay(500);
			e.onNext("B");
			delay(500);
			e.onNext("C");
			delay(500);
			e.onComplete();
	 	}	
	};
 
	public static ObservableOnSubscribe<Integer> source1_5 = new ObservableOnSubscribe<Integer>(){
		@Override
		public void subscribe(ObservableEmitter<Integer> e) throws Exception {
			for( int i=0; i<5;i++){
				delay( 500 );
				e.onNext(i);
			}
 			e.onComplete();
	 	}	
	};
	 
	 
//	 * Creating a source that starts a thread to avoid the blocking of subscribers is possible
//	 * but it is NOT A GOOD DESIGN. We should use RxJava concurrency declarative tools.
//	 * Nevertheless, this source
//	 * 	1) emits a string every 500 msecs without blocking the subscribers
//	 * 	2) ends if the subscriber has been disposed
	 
	public static ObservableOnSubscribe<String> sourceItemAsycnh = new ObservableOnSubscribe<String>(){
		@Override
		public void subscribe(ObservableEmitter<String> e) throws Exception {
			Runnable r = () ->{
				for( int i = 1; i<=5; i++ ){
					if( e.isDisposed() ) break;
					delay(500);
					String s = "item" + i;
					//log("sourceAsycnhNaive producing " + s);
					e.onNext(s);
				}
				e.onComplete();
			};
 			final Thread t = new Thread(r); //NOT A GOOD DESIGN
 			t.start();				
	 	}	
	};
	
	public static void delay( int dt ){
		try {
			Thread.sleep(dt);
//			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
	 		e.printStackTrace();
		}
	}		
	
 
	public static ObservableOnSubscribe<Integer> allNaturals = new ObservableOnSubscribe<Integer>(){
		@Override
		public void subscribe(ObservableEmitter<Integer> e) throws Exception {
			int i=0;
			while( ! e.isDisposed() ){
				delay( 500 );
				log("allNaturals -> " + i);
				e.onNext(i++);
			}
 			e.onComplete();
 	 	}	
	};

	public static ObservableOnSubscribe<String> clicks = new ObservableOnSubscribe<String>(){
		@Override
		public void subscribe(ObservableEmitter<String> e) throws Exception {
			int i=0;
			while( ! e.isDisposed() ){
				delay( 250 );
				String s = "click"+i++;
				e.onNext( s );
				log("clicks -> " + s);
			}
 			e.onComplete();
 	 	}	
	};
	
	public Observable<Integer> s1_20 = Observable.range(1,20);
 }
*/