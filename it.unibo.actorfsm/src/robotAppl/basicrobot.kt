package robotAppl
import kotlinx.coroutines.CoroutineScope
import utils.AppMsg
import fsm.Fsm
import utils.virtualRobotSupportApp
import utils.Messages
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
 

val ndnt   		= "&&& "
val backTime    = 80L

enum class basicrobotstate {
	stop, forward, backward, rleft, rright, obstacle
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class basicrobot ( name: String, scope: CoroutineScope,
				   usemqtt:Boolean=true,
				   val owner: Fsm?=null,
				   discardMessages:Boolean=true
				 ) : Fsm( name, scope, discardMessages,usemqtt){
 
	companion object{ 
		var rstate = basicrobotstate.stop	//here for testing purpose		
	}
	
	override fun getInitialState() : String{
		return "init"
	}
	
	override fun getBody() : (Fsm.() -> Unit){	
		return { //this:Fsm
			state("init") {	
				action { //it:State
					//virtualRobotSupportApp.traceOn = true
					virtualRobotSupportApp.setRobotTarget( myself  ) //Configure - Inject
					virtualRobotSupportApp.initClientConn()
					fsm.traceOn = false
					rstate = basicrobotstate.stop
					println("$ndnt basicrobot | STARTED in LOGICAL state=$rstate")
				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )
			}
			
			state("waitcmd"){
				action { //it:State
					//println("$ndnt basicrobot | waits ...")  
 				}
				transition( edgeName="t0",targetState="handlesensor", cond=whenDispatch("sensor") )				
				transition( edgeName="t1",targetState="endwork", cond=whenDispatch("end") )				
				transition( edgeName="t2",targetState="execcmd", cond=whenDispatch("cmd") )				
			}
			state("execcmd"){
				action { //it:State
					println("$ndnt basicrobot | exec ${currentMsg} in state=${currentState.stateName}")
					val move = currentMsg.CONTENT
					doMove( move )
 				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )
 			}
			state("handlesensor"){
				action{
					emit( currentMsg.MSGID, currentMsg.CONTENT  )
					if( currentMsg.CONTENT.startsWith("collision") ){
						println("$ndnt basicrobot | collision $currentMsg - moving back a little ...  ")
						doMove("s"); delay(backTime); doMove("h")	//robot reflex for safaety ...
						if( owner is Fsm ) forward( currentMsg, owner )
 						rstate = basicrobotstate.obstacle
					}
				}
				transition( edgeName="t0",targetState="waitcmd",  cond=doswitch()    )		
			}
			state("endwork") {
				action {
					virtualRobotSupportApp.terminate()
					println("basicrobot | endwork")
   					terminate()
  				}
 			}	 							

		}
	}
	
	suspend fun doMove( move: String ){
	  	virtualRobotSupportApp.doApplMove( move )
	  	when( move ){
	  		"w" -> rstate = basicrobotstate.forward
	  		"s" -> rstate = basicrobotstate.backward
	  		"r" -> rstate = basicrobotstate.rright
	  		"l" -> rstate = basicrobotstate.rleft
	  		"h" -> rstate = basicrobotstate.stop
	  	}
	}

}//basicrobot

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
	val robot = basicrobot("basicrobot", this, usemqtt=true, owner=null, discardMessages=false)

			delay(500) //wait for starting ...
			Messages.forward( "test","cmd", "r", robot   )
			delay(500)
			Messages.forward( "test","cmd", "l", robot    )
			delay(500)
			Messages.forward( "test","cmd", "w", robot    )
			delay(1000)
	delay(3000)
	robot.terminate()
	robot.waitTermination()
	println("main ends")
}