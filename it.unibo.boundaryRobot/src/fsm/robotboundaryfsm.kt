package fsm
//robotboundaryFsm
import kotlinx.coroutines.CoroutineScope
import utils.AppMsg

var nStep = 0		//Declared here for testing purpose
var nStop = 0

val goonMsg 	 = AppMsg.create("goon", "robotboundaryfsm", "robotboundaryfsm")	//self-message
val endMsg 	     = AppMsg.create("end",  "robotboundaryfsm", "robotboundaryfsm")	//self-message

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class robotboundaryfsm ( name: String, scope: CoroutineScope ) : Fsm( name, scope){
	
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
					println("robotboundary2 init connection with the virtual robot")
					virtualRobotSupportApp.initClientConn()
		 			nStep = 0
				}
				transition( edgeName="t0",targetState="working", cond=whenDispatch("start") )
				
			}			
			state("working") {
				action {
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
				transition( edgeName="t1",targetState="end", cond=whenDispatch("end") )
				transition( edgeName="t2",targetState="working", cond=whenDispatch("goon") )
			}			
			state("end") {
				action {
					println("robot terminates its job")
					fsmactor.close()
 				}
			}										
		}//return
	}//getBody	
}//robotboundaryfsm
 