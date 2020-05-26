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
 
object actorQakCoapObserver {

    private val client = CoapClient()
	
	private val ipaddr      = "localhost:8068"		//5683 default
	private val context     = "ctxboundaryplanned"
 	private val destactor   = "roomboudaryexplorer"
//	private val msgId       = "cmd"

	fun init(){
       val uriStr = "coap://$ipaddr/$context/$destactor"
	  println("actortQakCoapObserver | START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                println("actortQakCoapObserver | GET RESP-CODE= " + response.code + " content:" + response.responseText)
            }
            override fun onError() {
                println("actortQakCoapObserver | FAILED")
            }
        })		
	}

 }

 
 fun main( ) {
		actorQakCoapObserver.init()
		System.`in`.read()   //to avoid exit
 }

