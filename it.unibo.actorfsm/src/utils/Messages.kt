package utils

import kotlinx.coroutines.channels.SendChannel
import fsm.Fsm
 
object Messages{

	val initMsg        = AppMsg.create("init",    "main",	"robotboundary")
	val stopMsg        = AppMsg.create("stop",    "main",	"robotboundary")
	val startMsg       = AppMsg.create("start",   "main",	"robotboundary") 
	val resumeMsg      = AppMsg.create("resume",  "main",	"robotboundary")
	val workDoneMsg    = AppMsg.create("workdone","main",	"usermock"	   )
 
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun forward(  sender: String, msgId : String, payload: String, dest : Fsm ){
	 	//println("forward  msgId: ${msgId} payload=$payload")
		val msg = AppMsg.buildDispatch(actor=sender, msgId=msgId , content=payload, dest=dest.name)
 		if( ! dest.fsmactor.isClosedForSend) dest.fsmactor.send( msg  )
		else println("WARNING: Messages.forward attempts to send ${msg} to closed ${dest.name} ")
	}

	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun forward(  sender: String, msgId : String, payload: String, destName : String, mqtt: MqttUtils ){		
		val msg = AppMsg.buildDispatch(actor=sender, msgId=msgId , content=payload, dest=destName )
		if( mqtt.connectDone() ){
			mqtt.publish( "unibo/qak/${destName}", msg.toString() )
		}
	}
		
}