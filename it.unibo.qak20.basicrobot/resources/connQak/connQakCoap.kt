package connQak

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import org.eclipse.californium.core.coap.Request
import org.eclipse.californium.core.coap.CoAP.Code

class connQakCoap( hostIP : String,  port : String,  destName : String ) :
										           connQakBase(hostIP, port, destName){

lateinit var client   : CoapClient
lateinit var url : String
		
	override fun createConnection(  ){
 			println("connQakCoap | createConnection hostIP=${hostIP} port=${port}")
			url = "coap://$hostIP:$port/ctxbasicrobot/$destName"
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
	
	fun doPut(msg : String){
		 val post = Request(Code.PUT);
		 post.setURI( url )
		 post.setPayload( msg );
		 post.getOptions()
		   .setContentFormat(MediaTypeRegistry.TEXT_PLAIN)
		   .setAccept(MediaTypeRegistry.TEXT_PLAIN)
		   .setIfNoneMatch(true);
		 val response = post.send().waitForResponse(0)  //0 means 'forever' the time for the answer is - in general - NOT PREDICTABLE
   		 if( response !== null ) println("connQakCoap | answer put= ${response.getPayloadString()}")
		 else println("connQakCoap | sorry, no answer to put")
	}
	
	override fun request( move : String ){
		val msg = MsgUtil.buildRequest("connQakCoap", move,"$move(350)", destName)
		doPut( msg.toString() )
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