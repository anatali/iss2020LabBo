package connQak

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import consolegui.ctxqadest

class connQakCoap( hostIP : String,  port : String,  ctxDest : String, destName : String ) :
										           connQakBase(hostIP, port, ctxDest, destName){

lateinit var client   : CoapClient
	
	override fun createConnection(  ){
 			println("connQakCoap | createConnection hostIP=${hostIP} port=${port}")
			val url = "coap://$hostIP:$port/$ctxDest/$destName"
			client = CoapClient( url )
			client.setTimeout( 1000L )
 			//initialCmd: to make console more reactive at the first user cmd
 		    val respGet  = client.get( ) //CoapResponse
			if( respGet != null )
				println("connQakCoap | createConnection get | CODE=  ${respGet.code},  ${respGet.getResponseText()} ")
			else
				println("connQakCoap | url=  ${url} FAILURE")
	}
	
	override fun forward( msg: ApplMessage ){		
		println("connQakCoap | forward msg=${msg}")
        val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
        println("connQakCoap | PUT forward  RESPONSE CODE=  ${respPut.code}")		
	}
	
	override fun request( msg: ApplMessage ){
 		val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
		if( respPut != null )
  		System.out.println("connQakCoap | answer= ${respPut.getResponseText()}")				
	}
	
	override fun emit( msg: ApplMessage){
		val url = "coap://$hostIP:$port/ctx$destName"		//TODO
		client = CoapClient( url )
        //println("PUT emit url=${url} ")		
         val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
        //println("connQakCoap | PUT emit ${msg} RESPONSE CODE=  ${respPut.code}")		
		
	}	
}