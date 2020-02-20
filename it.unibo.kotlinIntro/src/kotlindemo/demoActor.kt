package kotlindemo
//demoActor.kt
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.SendChannel

class CounterMsg(
        val cmd:String,
        val response: CompletableDeferred<Int>?=null){
}

val counterActor : SendChannel<CounterMsg> = GlobalScope.actor<CounterMsg> {
    var localCounter = 0 // actor state
    //val v = channel.receive()
    for (msg in channel) { // iterate over incoming messages
        if( localCounter % 10000 == 0 )
            println("${msg.cmd} | $localCounter in ${curThread()} channel full=${channel.isFull}")
        when ( msg.cmd ) {
            "INC" -> localCounter++
            "DEC" -> localCounter--
            "GET" -> msg.response?.complete(localCounter)
            else -> throw Exception( "unknown" )
        }    }
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
    val initVal = CompletableDeferred<Int>()
    counterActor.send(CounterMsg("GET", initVal))
    println("Counter INITIAL VALUE=${initVal.await()}")
    massiveRun {
        counterActor.send(CounterMsg("INC") )
    }
    val finalVal = CompletableDeferred<Int>()
    counterActor.send(CounterMsg("GET", finalVal))
    println("Counter FINAL VALUE= = ${finalVal.await()}")
    counterActor.close() // shutdown the actor

    println("ENDS ${curThread()}")
}