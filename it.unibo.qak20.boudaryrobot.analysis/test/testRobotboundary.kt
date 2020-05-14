package test

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.MqttUtils
import coap.coapObserver
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse

class testRobotboundary {	
var robot  : ActorBasic? = null	
val initDelayTime     = 2000L   // 

	val context     = "ctxrobotboundary"
	val destactor   = "robotboundary"
	val addr        = "localhost:8078"
    val client      = CoapClient()
    val uriStr      = "coap://$addr/$context/$destactor"

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
   		kotlin.concurrent.thread(start = true) {
			it.unibo.ctxrobotboundary.main() // MainCtxrobotboundary
    	} 
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@After
	fun terminate() {
		println("testRobotboundary terminated ")
	}
		
	
	fun checkResource(value: String){
		val respGet : CoapResponse = client.get( )
		val v = respGet.getResponseText()
		println("	checkResource |  $v value=$value ")
 		assertTrue( v == value)
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
@Test
	fun testLocal(){
 		runBlocking{
			while( robot == null ){
				println(" --- testLocal waits for robot ... ---")
				delay(initDelayTime)  //give time to the actor to start
				robot = it.unibo.kactor.sysUtil.getActor(destactor)  
			}
 				client.uri = uriStr
				println("	testLocal |  $robot ")
	 			MsgUtil.sendMsg( "test","start", "start(0)", robot!!  )
	 			delay(500)
				checkResource("walk")
	 			MsgUtil.sendMsg( "test", "stop", "stop(0)", robot!! ) 
	 			delay(500)
				checkResource("stopped")
	 			MsgUtil.sendMsg( "test", "resume", "resume(0)", robot!! ) 
	 			delay(500)
				checkResource("walk")
	  			robot!!.waitTermination()
	 			checkResource(   "homeagain")
		}
}
}