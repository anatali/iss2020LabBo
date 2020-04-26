package connQak

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage

class connQakCoap( hostIP : String,  port : String,  destName : String ) :
										           connQakBase(hostIP, port, destName){

lateinit var client   : CoapClient
	
	override fun createConnection(  ){
 			println("connQakCoap | createConnection hostIP=${hostIP} port=${port}")
			val url = "coap://$hostIP:$port/ctxbasicrobot/$destName"
			client = CoapClient( url )
			client.setTimeout( 1000L )
 			//initialCmd: to make console more reactive at the first user cmd
 		    val respGet  = client.get( ) //CoapResponse
			if( respGet != null )
				println("connQakCoap | createConnection doing  get | CODE=  ${respGet.code}")
			else
				println("connQakCoap | url=  ${url} FAILURE")
	}
	
	override fun forward( move : String ){
 		val d = MsgUtil.buildDispatch("connQakCoap", "cmd", "cmd($move)", destName )
        val respPut = client.put(d.toString(), MediaTypeRegistry.TEXT_PLAIN)
        //if( respPut !== null ) println("connQakCoap | PUT forward ${d} RESPONSE CODE=  ${respPut.code}")
		
	}
	
	override fun request( move : String ){
		val msg = MsgUtil.buildRequest("connQakCoap", move,"$move(600)", destName)
		val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
  		if( respPut !== null ) println("connQakCoap | answer put= ${respPut.getResponseText()}")
		else println("connQakCoap | sorry, no answer to put")
		while(true){ //polling to acquire the answer
			Thread.sleep(100)
			val respGet = client.get( ).getResponseText()
			if( respGet.contains("reply")){
//				val answer = ApplMessage( respGet )
					println("connQakCoap | answer= ${respGet}")
					break
				}
			}	
 	}
	 
	override fun emit( ev : String ){
		val url = "coap://$hostIP:$port/ctx$destName"		//TODO
		client = CoapClient( url )
        //println("PUT emit url=${url} ")		
		val msg = MsgUtil.buildEvent("connQakCoap",ev,"$ev(fire)" )
        val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
		//if( respPut !== null ) println("connQakCoap | PUT emit ${msg} RESPONSE CODE=  ${respPut.code}")		
		
	}	
}