package kotlindemo
//demoActorCounter.kt
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.SendChannel


//Extension function on CoroutineScope
suspend fun CoroutineScope.manyRun( action: suspend () -> Unit ) {
    val n=100		//number of coroutines to launch
    val k=1000		//times an action is repeated by each coroutine
    val time = measureTimeMillis {
        val jobs = List(n) {
            launch { repeat(k) { action() }  }
        }
        jobs.forEach { it.join() } //wait for termination of all coroutines
    }
    println("Completed ${n * k} actions in $time ms")
}

class CounterMsg(
	val cmd:String,
	val response: CompletableDeferred<Int>?=null){
}

fun createCounter(scope : CoroutineScope):SendChannel<CounterMsg>{
 val counterActor = scope.actor<CounterMsg> {
	var k = 0 	//actor state
	for (msg in channel) { // iterate over incoming messages
	   if( k>0 && k % 10000 == 0  && msg.cmd != "GET" )
		println("${msg.cmd} | $k in ${curThread()} full=${channel.isFull}")
		when ( msg.cmd ) {
			"INC" -> k++
			"DEC" -> k--
			"GET" -> msg.response?.complete(k)
			else -> throw Exception( "unknown" )
		}
	  }
	}
 return counterActor
}

suspend fun getIntialValue(
		counterActor: SendChannel<CounterMsg>){
    val initVal = CompletableDeferred<Int>()
    counterActor.send(CounterMsg("GET", initVal))
    println("Counter INITIAL VALUE=${initVal.await()}")	
}

suspend fun sendManyMessages( scope : CoroutineScope, 
		counterActor: SendChannel<CounterMsg>){
	scope.manyRun {
		counterActor.send( CounterMsg("INC") )
    }
    val finalVal = CompletableDeferred<Int>()
    counterActor.send( CounterMsg("GET", finalVal) )
    println("Counter FINAL VALUE= = ${finalVal.await()}")	
}

fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
	
	val counter = createCounter( this )	
	getIntialValue( counter )
	
	sendManyMessages(this, counter)
	
	counter.close() //shutdown the actor

    println("ENDS ${curThread()}")
}