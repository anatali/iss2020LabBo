package test

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
import utils.Messages
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mapRoomKotlin.mapUtil

class TestPlanBoundary {
var refTestMap : String =""
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
		println("%%%  testBoundary prepare the result map expected ")
		mapRoomKotlin.buildRefTestMap()
		refTestMap = mapRoomKotlin.mapUtil.getMapAndClean()
 		println( refTestMap )
 		println("%%%  testBoundary activates the robot application ")
  		//TODO: activate the application
	}

	@After
	fun terminate() {
		println("%%%  testBoundary terminate ")
	}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun moveTest() {
		println("%%%  testBoundary activates the user simulator")
 		//TODO: activate an user simulator
	    //or send to the robotboundary the command start
 		println("%%%  testBoundary performs the final test after user end")		
		assertTrue( mapUtil.map.toString() ==  refTestMap )
	}
	
}//TestBoundary