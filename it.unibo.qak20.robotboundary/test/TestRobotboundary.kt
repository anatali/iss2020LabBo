package test

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mapRoomKotlin.mapUtil
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.MqttUtils
 

class TestRobotboundary {
var robot             : ActorBasic? = null
val mqttTest   	      = MqttUtils("test") 
val initDelayTime     = 3000L   // 

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
//		println("%%%  boudaryTest prepare the result map expected ")
//  		println( mapRoomKotlin.mapUtil.refMapForTesting )
   		//activate the application: SEE boundaryTest
   		kotlin.concurrent.thread(start = true) {
			it.unibo.ctxrobotboundary.main()  //WARNING: elininate the autostart
		}
	}

	@After
	fun terminate() {
		println("%%%  boudaryTest terminate ")
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun forwardToRobot(msgId: String, payload:String){
		println(" --- forwardToRobot --- $msgId:$payload")
		if( robot != null )  MsgUtil.sendMsg( "test",msgId, payload, robot!!  )
	}
	
	fun checkResource(value: String){		
		if( robot != null ){
			println(" --- checkResource --- ${robot!!.geResourceRep()}")
			assertTrue( robot!!.geResourceRep() == value)
		}  
	}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun testReqStart() {
		println("%%%  testReqStart  ")
		//TODO: activate an user simulator
	    //or send to the robotboundary the command start
 			robot = it.unibo.kactor.sysUtil.getActor("robotboundary")!!
  			//MsgUtil.sendMsg("main", "start","ok",robot)
			forwardToRobot("start", "start(0)" )
//			if( robot != null) robot!!.waitTermination()
	delay(7000)
// 		println("%%%  boudaryTest performs the final test after user end")		
		assertTrue( mapUtil.map.toString().equals( mapRoomKotlin.mapUtil.refMapForTesting ) )
	}
	


@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
@Test
	fun testRobotboundary(){
	 	runBlocking{
			delay(initDelayTime)  //time for robot to start
			robot = it.unibo.kactor.sysUtil.getActor("basicrobot")
			
 			testReqStart()
			
//			forwardToRobot( "end", "end(0)" )
//			delay( 500 )
//			checkResource("move(end)")
			if( robot != null ) robot!!.waitTermination()
		}
	 	println("testBasicRobot BYE  ")  
	}
	
}//testRobotboundary