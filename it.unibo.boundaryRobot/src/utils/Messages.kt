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
	suspend fun forward(  msg : AppMsg, dest : SendChannel<String> ){
	 	//println("forward msg: ${msg.MSGID} content=${msg.CONTENT} ")
	 	dest.send( msg.toString()  ) 
	}
	suspend fun forward(  msg : String, dest : SendChannel<String> ){
	 	//println("forward msg: ${msg} ")
	 	dest.send( msg  ) 
	}
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun forward(  msg : AppMsg, dest : Fsm ){
	 	//println("forward AppMsg msg: ${msg} ")
	 	if( ! dest.fsmactor.isClosedForSend) dest.fsmactor.send( msg  )
		else println("WARNING: Messages.forward attempts to send ${msg} to closed ${dest.name} ")
	}
	
//	suspend fun forward(  dest : SendChannel<String>, msgId : String, msgContent : String   ){
//		val m = AppMsg( msgId, AppMsgType.dispatch.toString(), "sender", dest)
//	}
	suspend fun forwardMqtt(  msg : AppMsg, mqttutils: MqttUtils, topic : String  ){
		//println("forwardMqtt msg: ${msg.MSGID} content=${msg.CONTENT} ")
 		mqttutils.publish( topic, msg.toString() )
	}
		
}