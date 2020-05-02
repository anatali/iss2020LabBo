package kotlincode

import org.eclipse.californium.core.CoapClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.eclipse.californium.core.CoapHandler
import org.eclipse.californium.core.CoapResponse
import kotlinx.coroutines.delay
import it.unibo.kactor.MqttUtils
import it.unibo.kactor.MsgUtil
import org.eclipse.californium.core.coap.MediaTypeRegistry
import kotlinx.coroutines.runBlocking

val ledurl    =  "coap://192.168.1.8:5683/GPIO/17/value"
val cmdurl    =  "coap://192.168.1.8:5683/devices/uno"
val client    =  CoapClient( cmdurl )
 
fun testRobotUsage(  ){
var v  = 0

 	while( v != 113){ //113='q'
		print("testRobotUsage>"  )
		v  = System.`in`.read()
		if( v > 96 &&  v < 123 ){
			val cmd = v.toChar()
			println("testRobotUsage cmd = ${cmd}"  )
			val path = "$cmdurl/$cmd"
		 	println("testRobotUsage $path"  )
			client.post(path, MediaTypeRegistry.TEXT_PLAIN)
 		}
	}
	println("testRobotUsage BYE"  )
}

fun main() {
		testRobotUsage( )
//		GlobalScope.launch{
//			var value = 0
// 			for ( v in 0..10 ) {
//				testRobotUsage(value)
//				if( value == 0 ) value = 1 else value = 0
//				delay(500)
//			}			
// 		}
}