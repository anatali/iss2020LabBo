package kotlindemo
//demoChannels
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce

suspend fun channelTest(){
    val timeElapsed = measureTimeMillis {
        val n = 5
        val channel = Channel<Int>(2)

        val sender = GlobalScope.launch {
            repeat( n ) {
                channel.send( it )
                println("SENDER | sent $it in ${curThread()}")
            }
        }
        delay(500) //The receiver starts after a while ...
        val receiver = GlobalScope.launch {
            for( i in 1..n ) {
                val v = channel.receive()
                println("RECEIVER | receives $v in ${curThread()}")
            }
        }

        delay(3000)
    }
    println("Done. time=$timeElapsed")
}
//-------------------- PROD CONS ------------------------
val simpleProducer : ReceiveChannel<Int> =
        GlobalScope.produce {
            for( i in 1..3 ){
                println( "simpleProducer produces $i in  ${curThread()}")
                send( i )
            }
        }

suspend fun consume(){
    val v = simpleProducer.receive()
    println( "consume receives ${v} in ${curThread()}" )
    simpleProducer.consumeEach {
        println( "consume receives $it in ${curThread()}" )
    }
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
    //channelTest()     //(1)
    consume()       //(2)
	Thread.sleep( 3000 )
    println("ENDS ${curThread()}")
}