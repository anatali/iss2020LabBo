package virtualRobotUsage
//robotActor.kt

import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
  
//Actor that includes the business logic; the behavior is message-driven 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
val robotActor  : SendChannel<String>	= CoroutineScope( Dispatchers.Default ).actor {
	var state    = "working"
	
	fun doInit() = virtualRobotSupport.initClientConn() 
	fun doEnd()  = { state = "end"  }
	fun doSensor(msg : String){ println("robotActor receives $msg") }
	
	suspend fun doCollision(msg : String){
		println("robotActor handles $msg going back a little");
		virtualRobotSupport.doApplMove( "s" )
		delay(500)		
		virtualRobotSupport.doApplMove( "h" )
	}
	
	fun doMove( move: String ){
  		virtualRobotSupport.doApplMove( move )		//move in the application-language 
	}
	
	while( state == "working" ){
		var msg = channel.receive()
		println("robotActor receives: $msg ")
		val applMsg = AppMsg.create(msg)
 		//println("robotActor applMsg.MSGID=${applMsg.MSGID} ")
		when( applMsg.MSGID ){
			"init"      -> doInit()
			"end"       -> doEnd()
			"sensor"    -> doSensor(msg)
			"collision" -> doCollision(msg)
			"move"      -> doMove(applMsg.CONTENT)
			else        -> println("NO HANDLE for $msg")
		}		
 	}//while
 	println("robotActor ENDS state=$state")
}

 

