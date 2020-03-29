package robotAppl
import kotlinx.coroutines.CoroutineScope
import utils.AppMsg
import fsm.Fsm
import utils.virtualRobotSupportApp
import utils.Messages
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import fsm.dummyactor

val ndnt   		= "    "
val backTime    = 80L

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class basicrobot ( name: String, scope: CoroutineScope, val owner: Fsm, discardMessages:Boolean=true,
				  	 usemqtt:Boolean=true ) : Fsm( name, scope, discardMessages,usemqtt){

	suspend fun doMove( move: String ){
	  	virtualRobotSupportApp.doApplMove( move )
	}

	override fun getInitialState() : String{
		return "init"
	}
	
	override fun getBody() : (Fsm.() -> Unit){	
		return { //this:Fsm
			state("init") {	
				action { //it:State
					//virtualRobotSupportApp.traceOn = true
					virtualRobotSupportApp.setRobotTarget( myself    ) //Configure - Inject
					virtualRobotSupportApp.initClientConn()
					fsm.traceOn = false
					println("$ndnt basicrobot | STARTED")
				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )
			}
			
			state("waitcmd"){
				action { //it:State
					//println("$ndnt basicrobot | waits ...")  
 				}
				transition( edgeName="t0",targetState="endwork", cond=whenDispatch("end") )				
				transition( edgeName="t0",targetState="execcmd", cond=whenDispatch("cmd") )				
			}
			state("execcmd"){
				action { //it:State
//					println("basicrobot | exec ${currentMsg} in state=${currentState.stateName}") 
					doMove( currentMsg.CONTENT )
				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )
 			}
			state("handlesensor"){
				action{
					if( currentMsg.CONTENT.startsWith("collision") ){
						println("$ndnt basicrobot | collision $currentMsg - moving back a little ...  ")
						doMove("s"); delay(backTime); doMove("h")	//robot reflex for safaety ...
						Messages.forward( currentMsg, owner )
 					}
				}
				transition( edgeName="t0",targetState="waitcmd",  cond=doswitch()    )		
			}
			state("endwork") {
				action {
					virtualRobotSupportApp.terminate()
   					terminate()
  				}
 			}	 							

		}
	}
}//basicrobot

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
	val a = dummyactor(this)
	val robot = basicrobot("basicrobot", this, a, discardMessages=false, usemqtt=true)
	//	robot.terminate()
	robot.waitTermination()
	println("main ends")
}