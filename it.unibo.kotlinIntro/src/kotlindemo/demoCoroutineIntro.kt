package kotlindemo
//demoCoroutinesIntro.kt
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.async
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.Job
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope

fun runBlockThread(){
    run { //Calls a function block; returns its result
        println("thread sleeps ... : ${curThread()}")
        Thread.sleep(1500)
        println("thread ends : ${curThread()}")
    } 
}

fun scopeDemo (){
	val scope = CoroutineScope( Dispatchers.Default )
	println( scope.coroutineContext )
	scope.launch{ runBlockThread() } 	
}
 

fun scopeAsyncDemo (){
	val scope = CoroutineScope( Dispatchers.Default )
	
	val res : Deferred<String>   = scope.async{
		println("async starts")
		Thread.sleep(3000)
		println("async ends")
		"hello from async"
	}
							 
	scope.launch{
		println("result waiting starts")
		val r = res.await(); //must be called only from a coroutine or a suspend function	
		println("result waiting result= ${r}")
	}
}

val n=10000		//number of Thread or Coroutines to launch
val k=1000		//times an action is repeated by each Thread or Coroutine

fun manyThreads(){
	var counter=0
 	var iter = 0
	val time = measureTimeMillis{
		val jobs = List(n){
			kotlin.concurrent.thread(start = true) {
				//println("thread ${iter++}starts ")
				repeat( k ){ counter++}
				//println("thread  $iter ends counter=$counter ")
			}
		}			
		jobs.forEach{ it.join()  }  //wait for termination of all threads
 	}
	println("manyThreads time= $time counter=$counter")
}

fun manyCoroutines(){
	val scope = CoroutineScope( Dispatchers.Default )
	var counter=0
	var iter = 0
	scope.launch{
	    val time = measureTimeMillis {
 	        val jobs = List(n) {
				//println("coroutine ${iter++} starts ")
				scope.launch { repeat(k) { counter++ } }
			}
			//println("manyCoroutines ENDS LANUCH ")
			jobs.forEach { it.join() }
			//println("manyCoroutines END ALL JOBS")
	    }
	    println("manyCoroutines time= $time counter=$counter")
	}
}
//    val time = measureTimeMillis {
//		val scope = CoroutineScope( Dispatchers.Default )
//        val jobs = List(n) { scope.launch { repeat(k) { counter++ } } }
//        scope.launch {
//			jobs.forEach { it.join() } //wait for termination of all coroutines
//			println("manyCoroutines joined ")
//		}
//	}
//	println("manyCoroutines time= $time counter=$counter")
 
//=================================================================
var demoTodo : () -> Unit = { println("nothing to do") }

fun readInt() : Int { print(">"); return readLine()!!.toInt() }

fun doDemo( input : Int ) = runBlocking{  
	println("BEGINS CPU=$cpus ${curThread()}")
	when( input ){
		1 ->  demoTodo =  { runBlockThread()                      	}
		2 ->  demoTodo =  { GlobalScope.launch{ runBlockThread() }	}
		3 ->  demoTodo =  { scopeDemo()								}
 		4 ->  demoTodo =  { scopeAsyncDemo()             	        }
 		5 ->  demoTodo =  { manyThreads()                	        }
  		6 ->  demoTodo =  { manyCoroutines()               	        }
		else ->  { println("command unknown") }  //Note the block  
	} 			
	println( "work done in time= ${measureTimeMillis(  demoTodo )}"  )
	println("ENDS ${curThread()}")	
}

fun main() {
		var input =  readInt()
		while( input != 0 ){
			doDemo( input )
			demoTodo = 	{ println("nothing to do") }    
			input    =  readInt()
		}
  	    println( "BYE") 
}
		
     
/*
    runBlockThread()                             //(1)

    GlobalScope.launch{ runBlockThread() }       //(2)
    println("BYE")                               //(2)

    Thread.sleep(1600) // To see the output      //(2)
 
    launch{  runBlockThread()  }                 //(3)
    println("BYE")                               //(3)
	Thread.sleep(3000)
 
    val job = launch{  runBlockThread()  }       //(4)
    job.join()                                   //(4)
    println("BYE")                               //(4)

    ioBoundFun()                              //(5)
    launch{ ioBoundFun() }                    //(6)
*/
    // activate()                              //(7)
/*
    println("BYE")
 */
     
