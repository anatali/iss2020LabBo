package virtualRobotUsage
//robotActorCril.kt

//import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
 
//Actor that includes the business logic; the behavior is message-driven 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
val robotActorCril  : SendChannel<String>	= CoroutineScope( Dispatchers.Default ).actor {
	var state    = "working"
	
	fun doInit() = virtualRobotSupport.initClientConn() 
	fun doEnd()  = { state = "end"  }
	fun doSensor(msg : String){ println("robotActorCril receives $msg") }
	
	suspend fun doCollision(msg : String){
		println("robotActorCril handles $msg going back a little");
		val goback =  "{ 'type': 'moveBackward', 'arg': 100 }"
		virtualRobotSupport.domove( goback  )  // not for plasticBox : the business logic is more complex ...
		delay(500)		
	}
	
	fun doMove(msgSplitted : List<String> ){
		val cmd = msgSplitted[1].replace(")","")
		virtualRobotSupport.domove( cmd )		
	}
	
	while( state == "working" ){
		var msg = channel.receive()
		println("robotActorCril receives: $msg ")
		val msgSplitted = msg.split('(')
		val msgFunctor  = msgSplitted[0]
		//println("robotActorCril msgFunctor $msgFunctor ")
		when( msgFunctor ){
			"init"      -> doInit()
			"end"       -> doEnd()
			"sensor"    -> doSensor(msg)
			"collision" -> doCollision(msg)
			"move"      -> doMove(msgSplitted)
			else        -> println("NO HANDLE for $msg")
		}		
 	}
 	println("robotActorCril ENDS state=$state")
}

 

