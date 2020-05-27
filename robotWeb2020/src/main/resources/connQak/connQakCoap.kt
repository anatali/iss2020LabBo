package connQak

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.coap.MediaTypeRegistry
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import org.eclipse.californium.core.CoapResponse
 

class connQakCoap( )  {

lateinit var client   : CoapClient
	
	 fun createConnection(  ){
 			System.out.println("connQakCoap | createConnection hostIP=${ConnConfig.hostAddr} port=${ConnConfig.port}")
			val url = "coap://${ConnConfig.hostAddr}:${ConnConfig.port}/${ConnConfig.ctxqadest}/${ConnConfig.qakdestination}"
			client = CoapClient( url )
			client.setTimeout( 1000L )
 			//initialCmd: to make console more reactive at the first user cmd
 		    val respGet  = client.get( ) //CoapResponse
			if( respGet != null )
				System.out.println("connQakCoap | createConnection doing  get | CODE=  ${respGet.code}")
			else
				System.out.println("connQakCoap | url=  ${url} FAILURE")
	}
	
	 fun forward( msg: ApplMessage ){		
        System.out.println("connQakCoap | PUT forward ${msg}  ")		
        val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
        System.out.println("connQakCoap | RESPONSE CODE=  ${respPut.code}")		
	}
	
	 fun request( msg: ApplMessage ){
 		val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
  		if( respPut != null ) System.out.println("connQakCoap | answer= ${respPut.getResponseText()}")		
		
	}
	
	 fun emit( msg: ApplMessage){
//		val url = "coap://$hostIP:$port/ctx$destName"		//TODO
//		client = CoapClient( url )
        //println("PUT emit url=${url} ")		
         val respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN)
         System.out.println("connQakCoap | PUT emit ${msg} RESPONSE CODE=  ${respPut.code}")		
		
	}
	
	 fun readRep(   ) : String{
		val respGet : CoapResponse = client.get( )
		return respGet.getResponseText()
	}
}