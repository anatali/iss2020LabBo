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
 
 

class testBasicrobot {
	
var robot             : ActorBasic? = null
val mqttTest   	      = MqttUtils("test") 
val initDelayTime     = 1000L   // 
val useMqttInTest 	  = false
val mqttbrokerAddr    = "tcp://broker.hivemq.com" 
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
		
   		kotlin.concurrent.thread(start = true) {
			it.unibo.ctxbasicrobot.main() // MainCtxBasicrobot()
			println("testBasicrobot systemSetUp done")
			//robot = basicrobot("basicrobot", GlobalScope, usemqtt=useMqttInTest )
   			if( useMqttInTest ){
				 while( ! mqttTest.connectDone() ){
					  println( "	attempting MQTT-conn to ${mqttbrokerAddr}  for the test unit ... " )
					  Thread.sleep(1000)
					  mqttTest.connect("test_nat", mqttbrokerAddr )					 
				 }
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
	suspend fun forwardToRobot(msgId: String, payload:String){
		println(" --- forwardToRobot --- $msgId:$payload")
		if( robot != null )  MsgUtil.sendMsg( "test",msgId, payload, robot!!  )
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun requestToRobot(msgId: String, payload:String){
		if( robot != null ){
			val msg = MsgUtil.buildRequest("test",msgId, payload,robot!!.name)
			MsgUtil.sendMsg( msg, robot!!  )		
		}  
	}
	
	fun checkResource(value: String){		
		if( robot != null ){
			println(" --- checkResource --- ${robot!!.geResourceRep()}")
			assertTrue( robot!!.geResourceRep() == value)
		}  
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun testReqCmd(){
		println("=========== testReqCmd =========== ")
 			forwardToRobot( "cmd", "cmd(r)" )
			delay(500)
			checkResource("move(r)")
			forwardToRobot( "cmd", "cmd(l)" )
			delay(500)
			checkResource("move(l)")
			forwardToRobot( "cmd", "cmd(w)" ) //ASSUMPTION: no obstacle
			delay(500)
			checkResource("move(w)")
			forwardToRobot( "cmd", "cmd(s)" ) //ASSUMPTION: no obstacle
			delay(500)
			checkResource("move(s)")
			forwardToRobot( "cmd", "cmd(h)" )
			delay(500)
			checkResource("move(h)")
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun testReqStep(){ //ASSUMPTION: no obstacle
		println(" ===========  testReqStep =========== ")
			requestToRobot("step","step(350)")
			delay(200)
			checkResource("step(350)") 
			delay(700)  //there is also stepPerhapsDone
			checkResource("stepDone")
	}
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun testReqSensor(){ //ASSUMPTION: obstacle
		println(" ===========  testReqSensor =========== ")
			forwardToRobot( "cmd", "cmd(w)" )
			delay(3000)
			checkResource("collision") 
 	}
 
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
@Test
	fun testBasicRobot(){
	 	runBlocking{
			while( robot == null ){
				delay(initDelayTime)  //time for robot to start
				robot = it.unibo.kactor.sysUtil.getActor("basicrobot")				
			}
			
			testReqCmd()
			delay( 1000 )
			testReqStep()
			delay( 1000 )
			testReqSensor()
			
			forwardToRobot( "end", "end(0)" )
			delay( 500 )
			checkResource("move(end)")
			if( robot != null ) robot!!.waitTermination()
		}
	 	println("testBasicRobot BYE  ")  
	}

}