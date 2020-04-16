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
val initDelayTime     = 1000L   // 

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
		println("%%%  TestRobotboundary terminate ")
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
	suspend fun testStopResume() {
		for( i in 1..4 ){
			delay(5000)
			forwardToRobot("stop", "stop($i)" )
			delay(3000)
			forwardToRobot("resume", "resume($i)" )
		}
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun testReqStart( withStop : Boolean=false ) {
		println("%%%  testReqStart $robot ")
		forwardToRobot("start", "start(0)" )
		if( withStop ) testStopResume()
		println("%%%  testReqStart waits ...  ")
 		if( robot != null) robot!!.waitTermination()
		assertTrue( mapUtil.map.toString().equals( mapRoomKotlin.mapUtil.refMapForTesting ) )
	}
	


@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
@Test
	fun testRobotboundary(){
	 	runBlocking{
 			while( robot == null ){
				println("testRobotboundary wait for robot ... ")
				delay(initDelayTime)  //time for robot to start
				robot = it.unibo.kactor.sysUtil.getActor("robotboundary")
 			}
			
			
 			testReqStart( withStop=false )
			
 			if( robot != null ) robot!!.waitTermination()
  		}
	 	println("testRobotboundary BYE  ")  
	}
	
}//testRobotboundary