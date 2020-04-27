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
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.launch 

object ch : CoapHandler {
            override fun onLoad(response: CoapResponse) {
                println("actorQakCoapObserver ch | GET RESP-CODE= " + response.code + " content:" + response.responseText)
            }
            override fun onError() {
                println("actorQakCoapObserver ch | FAILED")
            }
        } 
 
object actorQakCoapObserver {

    private val client = CoapClient()
    
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	 fun activate( context: String, destactor: String, ipaddr : String , owner: ActorBasic? = null){ 
       val uriStr = "$ipaddr/$context/$destactor"
 	   println("actortQakCoapObserver | START uriStr: $uriStr")
       client.uri = uriStr
	
//       client.get(ch, MediaTypeRegistry.TEXT_PLAIN)

	   client.observe(object : CoapHandler {
            override fun onLoad(response: CoapResponse) {
				val content = response.responseText
                println("actortQakCoapObserver | GET RESP-CODE= " + response.code + " content:" + content)
 				if(  owner!== null ) owner.scope.launch{
 					val event = MsgUtil.buildEvent( "observer","local_resrep","resrep('$content')")								
					owner.emit( event, avatar=true ) //to avoid that auto-event will be discarded
				}
           } 
            override fun onError() {
                println("actortQakCoapObserver | FAILED")
            }
        })		
	}

 }

 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) {
		actorQakCoapObserver.activate(
			"ctxresource", "resource", "localhost:8048" )
		System.`in`.read()   //to avoid exit
 }

