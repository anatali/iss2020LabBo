package firstSolution

import kotlinx.coroutines.channels.SendChannel
import utils.AppMsg

object Messages{

 	val startMsg       = AppMsg.create("start",   "main",	"robotboundary0") 
	val stopMsg        = AppMsg.create("stop",    "main",	"robotboundary0")
	val resumeMsg      = AppMsg.create("resume",  "main",	"robotboundary0")
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
		
}