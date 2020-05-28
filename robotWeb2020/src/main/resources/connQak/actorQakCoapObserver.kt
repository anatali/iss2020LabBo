package connQak

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import org.eclipse.californium.core.CoapHandler 
 
object actorQakCoapObserver {

    private val client = CoapClient()
	
	private val ipaddr      = "${configurator.hostAddr}:${configurator.port}"		//5683 default
	private val context     = configurator.ctxqadest
 	private val destactor   = configurator.qakdest
//	private val msgId       = "cmd"

	fun init(){
       val uriStr = "coap://$ipaddr/$context/$destactor"
	   System.out.println("actortQakCoapObserver | START uriStr: $uriStr")
       client.uri = uriStr
       client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                System.out.println("actortQakCoapObserver | GET RESP-CODE= " + response.code + " content:" + response.responseText)
            }
            override fun onError() {
                System.out.println("actortQakCoapObserver | FAILED")
            }
        })		
	}

 }

 
 fun main( ) {
		actorQakCoapObserver.init()
		System.`in`.read()   //to avoid exit
 }

