package fsm
//robotboundaryFsm
import kotlinx.coroutines.CoroutineScope
import utils.AppMsg
import utils.virtualRobotSupportApp

var nStep = 0		//Declared here for testing purpose
var nStop = 0

val goonMsg 	 = AppMsg.create("goon", "robotboundaryfsm", "robotboundaryfsm")	//self-message
val endMsg 	     = AppMsg.create("end",  "robotboundaryfsm", "robotboundaryfsm")	//self-message
val startMsg     = AppMsg.create("start", "user", "robotboundaryfsm")
val stopMsg      = AppMsg.create("stop", "user", "robotboundaryfsm")
val resumeMsg    = AppMsg.create("resume", "user", "robotboundaryfsm")

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class robotboundaryfsm ( name: String, scope: CoroutineScope, discardMessages:Boolean=true ) : Fsm( name, scope, discardMessages){
	
	override fun getInitialState() : String{
		return "init"
	}

	suspend fun doMove( move: String ){
	  	virtualRobotSupportApp.doApplMove( move )		//move in the application-language
	}
		
	override fun getBody() : (Fsm.() -> Unit){
		return {
			state("init") {
				action {  
					println("robotboundaryfsm init connection with the virtual robot")
					virtualRobotSupportApp.initClientConn()
		 			nStep = 0
				}
				transition( edgeName="t0",targetState="working", cond=whenDispatch("start") )				
			}			
			state("working") {
				action {
					println("robotboundaryfsm working")
					doMove("w")
				}
				transition( edgeName="t1",targetState="stopped", cond=whenDispatch("stop") )
				transition( edgeName="t2",targetState="endOfStep", cond=whenDispatch("sensor") )
			}
			state("endOfStep") {
				action {  
					nStep++
					doMove( "l" )
	 				if(nStep == 4) autoMsg (endMsg ) else autoMsg( goonMsg )  	
 				}
				transition( edgeName="t0",targetState="stopped", cond=whenDispatch("stop") )
				transition( edgeName="t1",targetState="end", cond=whenDispatch("end") )
				transition( edgeName="t2",targetState="working", cond=whenDispatch("goon") )
			}			
			state("end") {
				action {
					println("robot terminates its job")
					fsmactor.close()
 				}
			}										
			state("stopped") {
				action {
					doMove( "h" )
 				}
				transition( edgeName="t0",targetState="working", cond=whenDispatch("resume") )
			}										
		}//return
	}//getBody	
}//robotboundaryfsm
 