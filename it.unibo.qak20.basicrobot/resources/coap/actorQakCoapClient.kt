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
 
object actortQakCoapClient {

    private val client = CoapClient()
	
	private val context     = "ctxbasicrobot"
	private val sendactor   = "senderkt"
	private val destactor   = "basicrobot"
	private val msgId       = "cmd"

	fun init(){
       val uriStr = "coap://localhost:8018/$context/$destactor"
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
//        val uriStr = "coap://localhost:5683/ctxcoapdemo/actor0"
//        client.uri = uriStr
		val d = MsgUtil.buildDispatch("external", "cmd", "cmd($move)", "actor0" )
        val respPut = client.put(d.toString(), MediaTypeRegistry.TEXT_PLAIN)
        println("PUT ${d} RESPONSE CODE=  ${respPut.code}")
    }
}

fun console(){
	val read = Scanner(System.`in`)
	print("MOVE (h,w,s,r,l,z,x,a,d,q)>")
	var move = read.next()
	while( move != "q"){
		when( move ){
			"h" -> actortQakCoapClient.sendToServer("h")
			"w" -> actortQakCoapClient.sendToServer("w")
			"s" -> actortQakCoapClient.sendToServer("s")
			"r" -> actortQakCoapClient.sendToServer("r")
			"l" -> actortQakCoapClient.sendToServer("l")
			"x" -> actortQakCoapClient.sendToServer("x")
			"z" -> actortQakCoapClient.sendToServer("z")
			"a" -> actortQakCoapClient.sendToServer("a")
			"d" -> actortQakCoapClient.sendToServer("d")
 			else -> println("unknown")
		}
		print("MOVE (h,w,s,r,l,z,x,a,d,q)>")
		move = read.next()
	}
	print("BYE")
	System.exit(1)
}



fun main( ) = runBlocking  {
		actortQakCoapClient.init()
		
		console()
}

