package boundary
//boundary
/*
 --------------------------------------------------
 --------------------------------------------------
 */

import kotlinx.coroutines.channels.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import utils.AppMsg
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import utils.Messages
import fsm.Fsm
import stepper.stepper
 
var nStep = 0		//Declared here for testing purpose
var nStop = 0

enum class  RobotState{
	initial, working, endOfStep, stopped, end
}

/*
 ASSUMPTION: the room is empty
*/ 

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class boundaryrobot ( name: String, scope: CoroutineScope,
				   usemqtt:Boolean=false,
				   val owner: Fsm?=null,
				   discardMessages:Boolean=true
				 ) : Fsm( name, scope, discardMessages,usemqtt){

lateinit var robot : Fsm
var nstep = 0 	
 
	override fun getInitialState() : String{
		return "init"
	}
	
	override fun getBody() : (Fsm.() -> Unit){	
		return { //this:Fsm
			state("init") {	
				action { //it:State
					println("boundaryrobot | starts")
					robot = stepper("stepper", scope, usemqtt=false, owner=myself  )
				}
				transition( edgeName="t0",targetState="work", cond=whenDispatch("start") )
			}
			state("work") {	
				action { //it:State
					delay(500)
					forward( "step", "350", robot)
				}
				transition( edgeName="t0",targetState="work", cond=whenDispatch("stepdone") )
				transition( edgeName="t0",targetState="wall", cond=whenDispatch("stepfail") )
			}
			state("wall") {	
				action { //it:State
					nstep++
					println("boundaryrobot | obstacle at step $nstep")
					forward( "cmd", "l", robot)
					delay(500)
				}
				transition( edgeName="t0",targetState="work", cond=doswitchGuarded( {nstep<4}  ) )
				transition( edgeName="t0",targetState="end",  cond=doswitchGuarded( {nstep==4} ) )
 			}
			
			state("end") {	
				action { //it:State
					println("boundaryrobot | ends")
 				}
 			}
			
		}
	}

   	
 
/*	
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
					 Messages.forward(endMsg.toString(),channel)  //self-msg boundary not usable here	
				}else {
					 Messages.forward(goonMsg.toString(),channel) //self-msg boundary not usable here	
				}
				var transition = false
				while( !transition ){
	 				getInput()				
					when( curMsg.MSGID ){
						"end"   -> {   state = RobotState.end;     transition = true  }
						"goon"  -> {   state = RobotState.working; transition = true  }
						"stop"  -> {   state = RobotState.stopped; transition = true  }
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
	println("		Actor fsm |  TERMINATED in $state ")
}//fsm
	
	//Actor behavior		
	fsm()	
*/
}//boundary	
	
 

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main(  ) = runBlocking{
    println("==============================================")
    println("boundary | PLEASE, ACTIVATE WENV ")
    println("==============================================")
 
	val robot = boundaryrobot("boundaryrobot", this)
 	delay(1000) //give time to setup
	Messages.forward("main", "start","ok",robot)
	robot.waitTermination()
 	println("main | ENDS")
} 