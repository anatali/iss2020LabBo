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
                println("actorQakCoapObserver chhhhhhhhh | GET RESP-CODE= " + response.code + " content:" + response.responseText)
            }
            override fun onError() {
                println("actorQakCoapObserver chhhhhhhhh | FAILED")
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
	   client.get(ch, MediaTypeRegistry.TEXT_PLAIN)
	   client.observe( ch )
 
//	   client.observe(object : CoapHandler {
//            override fun onLoad(response: CoapResponse) {
//				val content = response.responseText
//                println("actortQakCoapObserver | GET RESP-CODE= " + response.code + " content:" + content)
// 				if(  owner!== null ) owner.scope.launch{
// 					val event = MsgUtil.buildEvent( "observer","local_resrep","resrep('$content')")								
//					owner.emit( event, avatar=true ) //to avoid that auto-event will be discarded
//				}
//           } 
//            override fun onError() {
//                println("actortQakCoapObserver | FAILED")
//            }
//        })		
	   
//	   for( i in 1..5 ){
// 			val r = MsgUtil.buildDispatch("coapalien", "turnOn", "turnOn(${i})", "led" )
//			val respPut = client.put(r.toString(), MediaTypeRegistry.TEXT_PLAIN)
//			println("PUT ${r} RESPONSE CODE=  ${respPut.code} ${respPut.getResponseText()}")
//		    Thread.sleep(1000)
//		    client.get(ch, MediaTypeRegistry.TEXT_PLAIN)
// 			val r1 = MsgUtil.buildDispatch("coapalien", "turnOff", "turnOff(${i})", "led" )
//			val respPut1 = client.put(r.toString(), MediaTypeRegistry.TEXT_PLAIN)
//			println("PUT ${r1} RESPONSE CODE=  ${respPut.code} ${respPut1.getResponseText()}")
//		    client.get(ch, MediaTypeRegistry.TEXT_PLAIN)   
//		    Thread.sleep(2000)
//	   }

       
    }

 }

 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) { //82.56.16.191
		actorQakCoapObserver.activate("ctxblsledalone", "led", "localhost:8080" )
		System.`in`.read()   //to avoid exit
 }

