package firstSolution
//robotboundary0
/*
 --------------------------------------------------
 WARNING: this is a 'naive' version to be revised
 --------------------------------------------------
 */
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import utils.virtualRobotSupport
import utils.AppMsg
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

var nStep = 0		//Declared here for testing purpose
var nStop = 0
var nmsg  = 0		//msg counter

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
val usermock0  : SendChannel<String>	= CoroutineScope( Dispatchers.Default ).actor {
	println("usermock0 | STARTS")
//	delay( 1300 )
	robotboundary0.send( Messages.startMsg.toString() )
	for( i in 1..5 ){
		delay( 1300 )
		println("usermock0 FORWARD STOP ${i}  nStep=$nStep");
		robotboundary0.send( Messages.stopMsg.toString()  )
		delay( 1500 )
		println("usermock0 FORWARD RESUME ${i}  nStep=$nStep");
		robotboundary0.send( Messages.resumeMsg.toString()  )
	}
	println("usermock0 | ENDS")
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
val robotboundary0  : SendChannel<String> = CoroutineScope( Dispatchers.Default).actor(capacity=50) {
 	
		suspend fun doMove( move: String ){
	  		virtualRobotSupport.doApplMove( move )		//move in the application-language
		}

		suspend fun doStop(   ){
			doMove("h")
			println("ROBOT STOPPED ${++nStop}")
		}
	
		suspend fun doResume(){
			println("robot RESUMED ${nStop}")
			doMove("w")	
		}
		suspend fun doEnd(){
			println("robot ENDS")
			channel.close()
		}
	
		suspend fun doInit(){
 			nStep       = 0
			println("robotboundary0 init connection with the virtual robot")
			virtualRobotSupport.initClientConn()
			doMove("w")	
		}

 		suspend fun elabSensor(msg : AppMsg){
			if( msg.CONTENT.startsWith("collision") ){	//defensive ...
				nStep++
				println("collision ${msg.CONTENT}  nStep=$nStep");
				doMove( "l" )
					if (nStep < 4){
						doMove("w")
					} else{
						  println("Lap completed since nStep=$nStep")
						  channel.send("msg(end,dispatch,main,robotboundary0,none,${nmsg++})") //selfmsg
					}
			}
		}
 	
/*
 -----------------------------------------
 		MESSAGE-DRIVEN BEHAVIOR
 -----------------------------------------
*/
 		println("robotboundary0 STARTS")
		for (msg in channel) { // iterate over incoming messages
 			println("robotboundary | receives: $msg  ")
			//msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
			val m = AppMsg.create(msg)
			when( m.MSGID ){
				"start"     -> { doInit()           	}
				"sensor"    -> { elabSensor( m )        }
				"stop"	    -> { doStop() 				}
				"resume"    -> { doResume()				}
				"end"       -> { doEnd()				}
			}		
		}	
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
    println("==============================================")
    println("robotboundary0 | PLEASE, ACTIVATE WENV ")
    println("==============================================")
  	virtualRobotSupport.setRobotTarget( robotboundary0  ) //Configure - Inject
	(robotboundary0 as Job).join()	//waits for termination of robotboundary0
	println("main | ENDS")
} 