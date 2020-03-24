package firstSolutionReviewed
//robotboundary1
/*
 --------------------------------------------------
 Revised version
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
import utils.Messages
 

var nStep = 0		//Declared here for testing purpose
var nStop = 0

enum class  RobotState{
	initial, working, endOfStep, stopped, end
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
val usermock1  : SendChannel<String>	=
		CoroutineScope( Dispatchers.Default ).actor(capacity=50) { //default capacity == 1
	
	println("usermock1 | STARTS")
 	val startMsg  = AppMsg.create("start", "usermock1", "robotboundary1")
	val stopMsg   = AppMsg.create("stop", "usermock1", "robotboundary1")
	val resumeMsg = AppMsg.create("resume", "usermock1", "robotboundary1")
	Messages.forward( startMsg.toString(), robotboundary1 )
	for( i in 1..5){
 	 	delay( 1300 )
		println("usermock1 FORWARD STOP ${i}  nStep=$nStep");
	 	Messages.forward( stopMsg.toString(), robotboundary1 )
	 	delay( 1500 )		
		println("usermock1 FORWARD RESUME ${i}  nStep=$nStep");
	 	Messages.forward( resumeMsg.toString(), robotboundary1 )
	}
	println("usermock1 | ENDS")
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
val robotboundary1  : SendChannel<String>	= CoroutineScope( Dispatchers.Default).actor(capacity=50) {
var state        = RobotState.initial
val goonMsg 	 = AppMsg.create("goon", "robotboundary1", "robotboundary1")	//self-message
val endMsg 	     = AppMsg.create("end",  "robotboundary1", "robotboundary1")	//self-message
	
		suspend fun doMove( move: String ){
	  		virtualRobotSupport.doApplMove( move )		//move in the application-language
		}

		suspend fun doInit(){	//Initial state
			println("robotboundary1 init connection with the virtual robot")
			virtualRobotSupport.initClientConn()
 			nStep       = 0
 		}

		suspend fun elabCollision( ){
			nStep++
			doMove( "l" )
		}
  	
/*
 -----------------------------------------
 		MESSAGE-BASED (FSM) BEHAVIOR
 -----------------------------------------
*/

	lateinit var curMsg : AppMsg
	
	suspend fun getInput()  {
  		var msg    = channel.receive()
 		curMsg     = AppMsg.create(msg)	
 	}
	
suspend fun fsm(   ){
	while( state != RobotState.end ){			
		when( state ){
 			RobotState.initial -> { doInit()
 									state = RobotState.working
 								  }   				

			RobotState.working   ->{
				getInput()
				when( curMsg.MSGID ){
					"start"     -> { state = RobotState.working
									 doMove("w")
					 			   }
					"stop"      -> { doMove("h")
						             state = RobotState.stopped
						             println("ROBOT STOPPED ${++nStop}")  		}
					"sensor"    -> {
									 if( curMsg.CONTENT.startsWith("collision") ){ //defensive ...
										 println("collision ${curMsg.CONTENT}  nStep=$nStep")
										 state = RobotState.endOfStep
									 }
								   }   
					 else -> println("fsm | in $state unexpects: $curMsg ")
				}
			}
			
			RobotState.endOfStep  -> {
				elabCollision(   )
 				if( nStep == 4 ){
					 Messages.forward(endMsg.toString(),channel)  //self-msg robotboundary1 not usable here	
				}else {
					 Messages.forward(goonMsg.toString(),channel) //self-msg robotboundary1 not usable here	
				}  
				getInput()				
				when( curMsg.MSGID ){
					"end"  ->  {   state = RobotState.end        }
					"goon"  -> {   state = RobotState.working
								   doMove("w")
 							   }
					"stop"  -> {   state = RobotState.stopped    }
				} 
 			}//endOfStep
			
			RobotState.stopped   ->{
				  getInput()
				  when( curMsg.MSGID ){
					"resume" -> { println("ROBOT RESUMED ${nStop}")
								  state = RobotState.working
								  doMove("w")
							    }
					else -> println("fsm |  in $state unexpects: $curMsg ")
				  }
			}
						  
			RobotState.end  ->  println("fsm |  should be never in $state ")
			}
	}//while
}//fsm
	
	//Actor behavior		
	fsm()	
	
}//robotboundary1	
	
 

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
    println("==============================================")
    println("robotboundary1 | PLEASE, ACTIVATE WENV ")
    println("==============================================")
  	virtualRobotSupport.setRobotTarget( robotboundary1  ) //Configure - Inject
	(robotboundary1 as Job).join()	//waits for termination of robotboundary0
	println("main | ENDS")
} 