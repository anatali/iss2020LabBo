package test
 	//"tcp://mqtt.eclipse.org:1883"
	//mqtt.eclipse.org
	//tcp://test.mosquitto.org
	//mqtt.fluux.io
	//"tcp://broker.hivemq.com" 

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

class testRobotboundary {
	
lateinit var robot    : ActorBasic
val mqttTest   	      = MqttUtils("test") 
val initDelayTime     = 3000L   // 
val useMqttInTest 	  = false
val mqttbrokerAddr    = "tcp://broker.hivemq.com"

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
   		kotlin.concurrent.thread(start = true) {
			it.unibo.ctxrobotboundary.main() // MainCtxBasicrobot()
			println("testBoundaryrobot systemSetUp done")
			//robot = basicrobot("basicrobot", GlobalScope, usemqtt=useMqttInTest )
   			if( useMqttInTest ){
				 while( ! mqttTest.connectDone() ){
					  println( "	attempting MQTT-conn to ${mqttbrokerAddr}  for the test unit ... " )
					  Thread.sleep(1000)
					  mqttTest.connect("test_nat", mqttbrokerAddr )					 
				 }
////				 sensorObserver("sensorobserver", GlobalScope, usemqtt=true )
			}	
   		} 
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@After
	fun terminate() {
		println("testBasicrobot terminated ")
	}
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
@Test
	fun testLocal(){
		println(" --- testLocal ---")
 		runBlocking{
			delay(initDelayTime)  //give time to the actor to start
			robot = it.unibo.kactor.sysUtil.getActor("robotboundary")!!
			coapObserver.init()
 			MsgUtil.sendMsg( "test","start", "start(0)", robot  )
 			delay(500)
			assertTrue(  robot.geResourceRep() == "moving")
 			MsgUtil.sendMsg( "test", "stop", "stop(0)", robot ) 
 			delay(500)
			assertTrue(  robot.geResourceRep() == "stopped")
 			MsgUtil.sendMsg( "test", "resume", "resume(0)", robot ) 
 			delay(500)
			assertTrue(  robot.geResourceRep() == "moving")
			val obstacle = MsgUtil.buildEvent("test", "collision","collision(wall)")
			for( i in 1..4){
				delay(800)
				println("test generates collision $i")
				MsgUtil.sendMsg( obstacle, robot ) 
			}
			
			
			robot.waitTermination()
// 			MsgUtil.sendMsg( "test", "end", "end(0)", robot ) 
			println("testLocal END with robot in  ${robot.geResourceRep()} ") //
			assertTrue(  robot.geResourceRep() == "terminated")
			 
		}
}
}