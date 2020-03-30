package robotAppl

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
import kotlinx.coroutines.GlobalScope
import fsm.Fsm
import utils.Messages
import utils.AppMsg
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import fsm.MqttUtils

class testBasicrobot {
	
lateinit var robot : Fsm
val mqtt   	= MqttUtils()
 
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
		kotlin.concurrent.thread(start = true) {	 
//			 robot = basicrobot("basicrobot", this, a, discardMessages=false, usemqtt=true)
 			 robot = basicrobot("basicrobot", GlobalScope, usemqtt=true )
			 if( robot.usemqtt ) mqtt.connect("test", robot.mqttbrokerAddr )
			  //fsm.traceOn = true
 		}		
	} 
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@After
	fun terminate() {
		robot.terminate()
		println("TestRobotboundaryFsm terminated ")
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testCmd(){
		runBlocking{
			delay(2000)  //time for robot to connect to mqtt broker
			//Messages.forward( "test","cmd", "r", robot, mqtt  )
			Messages.forward( "test","cmd", "r", robot.name, mqtt  )
			delay(1000)
			//Messages.forward( "test","cmd", "l", robot, mqtt  )
			Messages.forward( "test","cmd", "l", robot.name, mqtt  )
			delay(1000)
			println("testCmd BYE ")
		}
	}
	
}