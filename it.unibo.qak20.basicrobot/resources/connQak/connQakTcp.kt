package connQak

import it.unibo.supports.FactoryProtocol
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.kactor.MsgUtil

class connQakTcp( hostIP : String,  port : String,  destName : String ) :
										           connQakBase(hostIP, port, destName){
	lateinit var conn   : IConnInteraction
	
	override fun createConnection( ){ //hostIP: String, port: String  
		val fp    = FactoryProtocol(null,"TCP","connQakTcp")
	    conn      = fp.createClientProtocolSupport(hostIP, port.toInt() )    
	}
	
	override fun forward( move : String ){
		val msg = MsgUtil.buildDispatch("connQakTcp","cmd","cmd($move)", destName)
		conn.sendALine( msg.toString()  )				
	}
	
	override fun request( move : String ){
		val msg = MsgUtil.buildRequest("connQakTcp", move,"$move(600)", destName)
		conn.sendALine( msg.toString()  )
		//Acquire the answer	
		val answer = conn.receiveALine()
		println("connQakTcp | answer= $answer")		
	}
	
	override fun emit( ev : String ){
		val msg = MsgUtil.buildEvent("connQakTcp",ev,"$ev(0)" )
		conn.sendALine( msg.toString()  )			
	}	
}