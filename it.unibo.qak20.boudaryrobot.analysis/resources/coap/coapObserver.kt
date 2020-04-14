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
 
object coapObserver {

    private val client = CoapClient()
	
	private val ipaddr      = "localhost:8018"		//5683 default
	private val context     = "ctxboundaryrobot"	//"ctxbasicrobot"
 	private val destactor   = "boundaryrobot" 		//"basicrobot"
	private val msgId       = "cmd"
	private val tt          = "               coapObserver | "
	fun init(){
       val uriStr = "coap://$ipaddr/$context/$destactor"
	  println("$tt START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                //println("$tt GET RESP-CODE= " + response.code + " content:" + response.responseText)
				println("$tt  ${response.responseText}" )
            }
            override fun onError() {
                println("$tt  FAILED")
            }
        })		
	}

 }

 
 fun main( ) {
		coapObserver.init()
		System.`in`.read()   //to avoid exit
 }

