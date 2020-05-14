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
import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
 
 

class testVirtualrobot {
	
var robot             : ActorBasic? = null
val mqttTest   	      = MqttUtils("test") 
val initDelayTime     = 1500L   // 
val useMqttInTest 	  = false
val mqttbrokerAddr    = "tcp://broker.hivemq.com"
	
	val context     = "ctxvirtualdemo"
	val destactor   = "virtualrobot"
	val addr        = "localhost:8014"
    val client      = CoapClient()
    val uriStr      = "coap://$addr/$context/$destactor"
       
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() { 		
   		kotlin.concurrent.thread(start = true) {
			client.uri = uriStr
			it.unibo.ctxvirtualdemo.main() // MainCtxvirtualdemo
			println("testVirtualrobot systemSetUp done")
//    		if( useMqttInTest ){
//				 while( ! mqttTest.connectDone() ){
//					  println( "	attempting MQTT-conn to ${mqttbrokerAddr}  for the test unit ... " )
//					  Thread.sleep(1000)
//					  mqttTest.connect("test_nat", mqttbrokerAddr )					 
//				 }
// 			}	
 	} 
}	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@After
	fun terminate() {
		println("testVirtualrobot terminated ")
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
		//OLD VERSION, quite 'efficient' but logically questionable	
//		if( robot != null ){
//			println(" --- checkResource --- ${robot!!.geResourceRep()} value=$value")
// 			assertTrue( robot!!.geResourceRep() == value)
//		}
		val respGet : CoapResponse = client.get( )
		val v = respGet.getResponseText()
		println("	checkResource |  $v value=$value ")
		assertTrue( v == value)
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
			delay(300)
			checkResource("move(s)")
			forwardToRobot( "cmd", "cmd(h)" )
			delay(500)
			checkResource("move(h)")
	}

		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun testReqSensor(){ //ASSUMPTION: obstacle
		println(" ===========  testReqSensor =========== ")
			forwardToRobot( "cmd", "cmd(w)" )
			delay(3000)
			checkResource("obstacle") 
 	}
 
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
@Test
	fun testVirtualRobot(){
	 	runBlocking{
			while( robot == null ){
				delay(initDelayTime)  //time for robot to start
				robot = it.unibo.kactor.sysUtil.getActor("virtualrobot")				
			}
			delay( 1000 )
//			checkResource("stopped") 
			testReqCmd()
//			delay( 1000 )
////			testReqStep()						//not (yet) implemented
////			delay( 1000 )
//			testReqSensor()
//			
//			forwardToRobot( "end", "end(0)" )	//not (yet) implemented
//			delay( 500 )
// 			checkResource("move(end)")
//			if( robot != null ) robot!!.waitTermination()
		}
	 	println("testVirtualRobot BYE  ")  
	}

}