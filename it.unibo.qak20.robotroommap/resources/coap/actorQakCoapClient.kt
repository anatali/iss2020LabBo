package coap

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import java.util.Scanner
import org.eclipse.californium.core.CoapHandler 
 
object actorQakCoapClient {

    private val client = CoapClient()
	
	private val context     = "ctxboundaryplanned"
	private val sendactor   = "senderkt"
	private val destactor   = "roomboundaryexplorer"
	private val msgId       = "cmd"

	fun init(){
       val uriStr = "coap://localhost:8020/$context/$destactor"
       client.uri = uriStr
//       client.observe(object : CoapHandler {
//            override fun onLoad(response: CoapResponse) {
//                println("ASYNCH GET RESP-CODE= " + response.code + " content:" + response.responseText)
//            }
//            override fun onError() {
//                println("FAILED")
//            }
//        })		
	}

	fun sendToServer(move: String) {
		if( move == "p" ){
			val r = MsgUtil.buildRequest("coapalien", "step", "step(350)", "basicrobot" )
			val respPut = client.put(r.toString(), MediaTypeRegistry.TEXT_PLAIN)
			println("PUT ${r} RESPONSE CODE=  ${respPut.code}")
		}else{
			val d = MsgUtil.buildDispatch("coapalien", "cmd", "cmd($move)", "basicrobot" )
	        val respPut = client.put(d.toString(), MediaTypeRegistry.TEXT_PLAIN)
	        println("PUT ${d} RESPONSE CODE=  ${respPut.code}")
		}
    }
}

fun console(){
	val read = Scanner(System.`in`)
	print("MOVE (h,w,s,r,l,z,x,a,d,p,q)>")
	var move = read.next()
	while( move != "q"){
		when( move ){
			"h" -> actorQakCoapClient.sendToServer("h")
			"w" -> actorQakCoapClient.sendToServer("w")
			"s" -> actorQakCoapClient.sendToServer("s")
			"r" -> actorQakCoapClient.sendToServer("r")
			"l" -> actorQakCoapClient.sendToServer("l")
			"x" -> actorQakCoapClient.sendToServer("x")
			"z" -> actorQakCoapClient.sendToServer("z")
			"p" -> actorQakCoapClient.sendToServer("p")
			"d" -> actorQakCoapClient.sendToServer("d")
			"a" -> actorQakCoapClient.sendToServer("a")
 			else -> println("unknown")
		}
		print("MOVE (h,w,s,r,l,z,x,a,d,q)>")
		move = read.next()
	}
	print("BYE")
	System.exit(1)
}



fun main( ) = runBlocking  {
		actorQakCoapClient.init()
		
		console()
}

