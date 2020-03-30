package robotAppl

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
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
			 //fsm.traceOn = true
 			 robot = basicrobot("basicrobot", GlobalScope, usemqtt=false )
 			 if( robot.usemqtt )
				 if( ! mqtt.connect("test", robot.mqttbrokerAddr ) )					  
					 	fail( "MQTT failure" )
			     else println("test MQTT connected")
 		}		
	} 
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@After
	fun terminate() {
		println("TestRobotboundaryFsm terminated ")
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun testObstacleLocal(){
 		runBlocking{
			delay(1000)  //time for robot to connect to mqtt broker
 			Messages.forward( "test","cmd", "w", robot   )
 			delay(1500)
			//AFTER obstacle
  			assertTrue( basicrobot.rstate == basicrobotstate.obstacle)			
			Messages.forward( "test", "end", "end", robot   )
			println("testObstacleLocal END with robot in  ${basicrobot.rstate}")
			robot.waitTermination()
		}
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun testObstacleRemote(){
 		runBlocking{
			delay(1000)  //time for robot to connect to mqtt broker
 			Messages.forward( "test","cmd", "w", robot.name, mqtt   )
 			delay(1500)
			//AFTER obstacle
  			assertTrue( basicrobot.rstate == basicrobotstate.obstacle)			
			Messages.forward( "test", "end", "end", robot.name, mqtt   )
			println("testObstacleRemote END with robot in  ${basicrobot.rstate}")
			robot.waitTermination()
		}
}
				
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun testLocal(){
 		runBlocking{
			delay(1000)  //time for robot to connect to mqtt broker
//			Messages.forward( "test","cmd", "r", robot   )
//			delay(500)
// 			assertTrue( basicrobot.rstate == basicrobotstate.rright)
//			Messages.forward( "test","cmd", "l", robot   )
//			delay(500)
// 			assertTrue( basicrobot.rstate == basicrobotstate.rleft)
//			Messages.forward( "test","cmd", "w", robot   )
// 			delay(100)
// 			assertTrue( basicrobot.rstate == basicrobotstate.forward)
//			delay(600)
// 			Messages.forward( "test","cmd", "h", robot   )
//			delay(100)
//			println("testLocal after h state=${basicrobot.rstate}")
//  			assertTrue( basicrobot.rstate == basicrobotstate.stop)
//			delay(1000)
 			Messages.forward( "test","cmd", "w", robot   )
 			delay(1500)
			//AFTER obstacle
			println("testLocal final state=${basicrobot.rstate}")
  			assertTrue( basicrobot.rstate == basicrobotstate.obstacle)			
 			Messages.forward( "test","end", "end", robot   )
			println("testLocal END with robot in  ${basicrobot.rstate}")
			robot.waitTermination()
 		}
	}
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
		fun testRemote(){
 		runBlocking{
			delay(2000)  //time for robot to connect to mqtt broker
//			Messages.forward( "test","cmd", "r", robot.name, mqtt  )
//			delay(500)
//			Messages.forward( "test","cmd", "l", robot.name, mqtt  )
//			delay(500)
			Messages.forward( "test","cmd", "w", robot.name, mqtt  )
			delay(600)
 			Messages.forward( "test","cmd", "h", robot.name, mqtt  )
			delay(1500)
// 			assertTrue( basicrobot.rstate == basicrobotstate.stop)
			delay(1000)
 			Messages.forward( "test","cmd", "w", robot.name, mqtt  )
//			delay(1000)
//			//Messages.forward( "test","cmd", "l", robot, mqtt  )
//			Messages.forward( "test","cmd", "l", robot.name, mqtt  )
//			delay(1000)
 			//assertTrue( basicrobot.rstate == basicrobotstate.waiting)
			robot.waitTermination()			
		}
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testCmd(){
			//testLocal()
			testObstacleLocal()
			//testObstacleRemote()
 			//println("testCmd BYE with robot in  ${basicrobot.rstate}")
	}

}