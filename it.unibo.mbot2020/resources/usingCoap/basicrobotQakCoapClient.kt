package usingCoap

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import java.util.Scanner 
 

object basicrobotQakCoapClient {

    private val client = CoapClient()
	
	private val context     = "ctxRobot"
	private val sendactor   = "senderkt"
	private val destactor   = "basicrobot"
	private val msgId       = "cmd"
	
    private fun sendToServerResource(msg: ApplMessage) {
		/*
 			path = contextName / actorName
 		*/
        val uriStr = "coap://192.168.1.7/$context/${msg.msgReceiver()}"  
        client.uri = uriStr
  	    val payload = msg.toString() //msg.msgId()
        println("basicrobotQakCoapClient | USING: $uriStr payload=" + payload)
        val res = client.put(payload, MediaTypeRegistry.TEXT_PLAIN)
        println("basicrobotQakCoapClient | RESPONSE=" + res.responseText)
    }
	
	fun bm( move : String ) : ApplMessage {
		//MsgUtil.buildDispatch(name, msgId, msg, destActor.name )
		return MsgUtil.buildDispatch(sendactor, msgId, move, destactor ) 
	}

    fun h() { sendToServerResource( bm("h") )  }
    fun w() { sendToServerResource( bm("w") )  }
    fun s() { sendToServerResource( bm("s") )  }
    fun r() { sendToServerResource( bm("r") )  }
    fun l() { sendToServerResource( bm("l") )  }
    fun x() { sendToServerResource( bm("x") )  }
    fun z() { sendToServerResource( bm("z") )  }
    fun a() { sendToServerResource( bm("a") )  }
    fun d() { sendToServerResource( bm("d") )  }


}

fun console(){
	val read = Scanner(System.`in`)
	print(">")
	var move = read.next()
	while( move != "q"){
		when( move ){
			"h" -> basicrobotQakCoapClient.h()
			"w" -> basicrobotQakCoapClient.w()
			"s" -> basicrobotQakCoapClient.s()
			"r" -> basicrobotQakCoapClient.r()
			"l" -> basicrobotQakCoapClient.l()
			"x" -> basicrobotQakCoapClient.x()
			"z" -> basicrobotQakCoapClient.z()
			"a" -> basicrobotQakCoapClient.a()
			"d" -> basicrobotQakCoapClient.d()
 			else -> println("unknown")
		}
		print(">")
		move = read.next()
	}
	print("BYE")
	System.exit(1)
}

    fun main( ) = runBlocking  {
		console()
    }

