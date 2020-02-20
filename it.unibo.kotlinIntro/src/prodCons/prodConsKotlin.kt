package prodCons

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

@kotlinx.coroutines.ObsoleteCoroutinesApi
val context = newSingleThreadContext("myThread")

@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
val producer: ReceiveChannel<Any> =
    GlobalScope.produce(context, 0){
        println( "producer sends 5.2   in ${curThread()}")
        send(5.2)
        println( "producer sends a   in ${curThread()}")
        send("a")
        println( "producer sends 100 in ${curThread()}")
        send(100)
    }

@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
suspend fun consumer(){
    val v = producer.receive()
    println( "consumer receives1 $v in ${curThread()}")
    producer.consumeEach { println( "consumer receives2 $it in ${curThread()}") }
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{ 
	println( "BEGIN")
    consumer()
    println( "END")
}
