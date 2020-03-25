package firstSolutionReviewed2
//robotboundary2
/*
 --------------------------------------------------
 Revised version 2:
 the robotboundary actor is able to properly handle
 (discard) unexpected messages
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
val usermock2  : SendChannel<String>	=
		CoroutineScope( Dispatchers.Default ).actor(capacity=50) { //default capacity == 1
	
	println("usermock1 | STARTS")
 	val startMsg  = AppMsg.create("start", "usermock1", "robotboundary2")
	val stopMsg   = AppMsg.create("stop", "usermock1", "robotboundary2")
	val resumeMsg = AppMsg.create("resume", "usermock1", "robotboundary2")
//Simulate a 'wrong' message: stop before start		
 	Messages.forward( stopMsg.toString(), robotboundary2 )
	delay( 500 ) 
	Messages.forward( startMsg.toString(), robotboundary2 )
//Simulate a 'wrong' message: resume after start		
		delay( 200 ) 
	 	Messages.forward( resumeMsg.toString(), robotboundary2 )
	for( i in 1..5){
 	 	delay( 1300 )
		println("usermock1 FORWARD STOP ${i}  nStep=$nStep");
	 	Messages.forward( stopMsg.toString(), robotboundary2 )
//Simulate a 'wrong' message: stop after a stop		
		delay( 200 ) 
		Messages.forward( stopMsg.toString(), robotboundary2 )
	 	delay( 1400 )		
		Messages.forward( stopMsg.toString(), robotboundary2 )
		println("usermock1 FORWARD RESUME ${i}  nStep=$nStep");
	 	Messages.forward( resumeMsg.toString(), robotboundary2 )
//Simulate a 'wrong' message: resume after a resume			
		delay( 200 )
		Messages.forward( resumeMsg.toString(), robotboundary2 )
	} 
	println("usermock1 | ENDS")
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
val robotboundary2  : SendChannel<String>	= CoroutineScope( Dispatchers.Default).actor(capacity=50) {
var state        = RobotState.initial
val goonMsg 	 = AppMsg.create("goon", "robotboundary2", "robotboundary2")	//self-message
val endMsg 	     = AppMsg.create("end",  "robotboundary2", "robotboundary2")	//self-message
	
		suspend fun doMove( move: String ){
	  		virtualRobotSupport.doApplMove( move )		//move in the application-language
		}

		suspend fun doInit(){	//Initial state
			println("robotboundary2 init connection with the virtual robot")
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
	
	
	fun discardMsg(){
		println("		WARNING: fsm | in $state unexpects: $curMsg ")
	}
	
suspend fun fsm(   ){
	while( state != RobotState.end ){			
		when( state ){
 			RobotState.initial -> { doInit()
   									var transition=false
   									while( !transition ){
     									getInput()
	 									when( curMsg.MSGID ){
	 										"start"  -> { state = RobotState.working; transition=true }
											 else -> discardMsg() //must not redo the action
										}
									}
								  }   				
			RobotState.working -> {
				doMove("w")
				var transition=false
				while( !transition ){
					getInput()
					when( curMsg.MSGID ){
						"stop"      -> { doMove("h")
							             state = RobotState.stopped; transition=true;
							             println("ROBOT STOPPED ${++nStop}")
										}
						"sensor"    -> {
										 if( curMsg.CONTENT.startsWith("collision") ){ //defensive ...
											 println("collision ${curMsg.CONTENT}  nStep=$nStep")
											 state = RobotState.endOfStep; transition=true;
										 }
									   }   
						 else -> discardMsg() //must not redo the action 
					}
				}
			}
			
			RobotState.endOfStep  -> {
				elabCollision(   )
 				if( nStep == 4 ){
					 Messages.forward(endMsg.toString(),channel)  //self-msg robotboundary2 not usable here	
				}else {
					 Messages.forward(goonMsg.toString(),channel) //self-msg robotboundary2 not usable here	
				}
				var transition = false
				while( !transition ){
	 				getInput()				
					when( curMsg.MSGID ){
						"end"   -> {   state = RobotState.end; transition = true  }
						"goon"  -> {   state = RobotState.working; transition = true    }
						"stop"  -> {   state = RobotState.stopped; transition = true    }
						 else -> discardMsg() 	 //must not redo the action 
					}
				} 
 			}//endOfStep
			
			RobotState.stopped   ->{
				  getInput()
				  when( curMsg.MSGID ){
					"resume" -> { println("ROBOT RESUMED ${nStop}")
								  state = RobotState.working
								  doMove("w")
							    }
					else -> discardMsg()
				  }
			}
						  
			RobotState.end  ->  println("		ERROR: fsm |  should be never in $state ")
			}
	}//while
}//fsm
	
	//Actor behavior		
	fsm()	
	
}//robotboundary2	
	
 

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
    println("==============================================")
    println("robotboundary2 | PLEASE, ACTIVATE WENV ")
    println("==============================================")
  	virtualRobotSupport.setRobotTarget( robotboundary2  ) //Configure - Inject
	(robotboundary2 as Job).join()	//waits for termination of robotboundary0
	println("main | ENDS")
} 