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
lateinit var robot    : ActorBasic
val mqttTest   	      = MqttUtils("test") 
val initDelayTime     = 3000L   // 

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
		println("%%%  boudaryTest prepare the result map expected ")
  		println( mapRoomKotlin.mapUtil.refMapForTesting )
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
@Test
	fun boudaryTest() {
		println("%%%  boudaryTest activates the application")
		//TODO: activate an user simulator
	    //or send to the robotboundary the command start
		runBlocking{
			delay(initDelayTime)  //give time to the actor to start
			robot = it.unibo.kactor.sysUtil.getActor("robotboundary")!!
  			MsgUtil.sendMsg("main", "start","ok",robot)
			robot.waitTermination()
		}
		println("%%%  boudaryTest performs the final test after user end")		
		assertTrue( mapUtil.map.toString().equals( mapRoomKotlin.mapUtil.refMapForTesting ) )
	}
	
}//boudaryTest