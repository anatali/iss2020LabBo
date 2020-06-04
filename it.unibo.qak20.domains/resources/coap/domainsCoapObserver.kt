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
 
object domainsCoapObserver {

    private val client = CoapClient()
	
	private val ipaddr      = "localhost:8060"		//5683 default
	private val context     = "ctxdomains"
 	private val destactor   = "waiter"
//	private val ipaddr      = "localhost:8020"		//5683 default
//	private val context     = "ctxbasicrobot"
// 	private val destactor   = "basicrobot"
 

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
		domainsCoapObserver.init()
		System.`in`.read()   //to avoid exit
 }

