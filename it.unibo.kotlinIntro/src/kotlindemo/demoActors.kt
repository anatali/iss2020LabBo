package kotlindemo
//demoActors.kt
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.SendChannel

var senderActor   : SendChannel<String>?  = null
var receiverActor : SendChannel<String>?  = null

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun startReceiver( scope : CoroutineScope){
	receiverActor = scope.actor<String> {  //actor is a coroutine builder (dual of produce)
		println("receiverActor STARTS")
		var msg = channel.receive()
		while( msg != "end"){
			println("receiverActor receives $msg")
			msg = channel.receive()
		}
		println("receiverActor ENDS")
	}
}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun startSender( scope : CoroutineScope){
	senderActor = scope.actor<String> { //actor is a coroutine builder (dual of produce)
		println("senderActor STARTS")
 		receiverActor!!.send("Hello1")
		delay(500)
 		receiverActor!!.send("Hello2")
		delay(500)
		receiverActor!!.send("end")
		println("senderActor ENDS")
 	}
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
 	startReceiver( this )
	startSender( this )
    println("ENDS ${curThread()}")
}