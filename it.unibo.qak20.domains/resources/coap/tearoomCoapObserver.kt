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
 
object tearoomCoapObserver {

    private val client = CoapClient()
	
	private val ipaddr      = "localhost:8050"		//5683 default
	private val context     = "ctxtearoom"
 	private val destactor   = "teatables"
 

	fun init(){
       val uriStr = "coap://$ipaddr/$context/$destactor"
	  println("tearoomCoapObserver | START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                println("tearoomCoapObserver | GET RESP-CODE= " + response.code + " content:" + response.responseText)
            }
            override fun onError() {
                println("tearoomCoapObserver | FAILED")
            }
        })		
	}

 }

 
 fun main( ) {
		tearoomCoapObserver.init()
		System.`in`.read()   //to avoid exit
 }

