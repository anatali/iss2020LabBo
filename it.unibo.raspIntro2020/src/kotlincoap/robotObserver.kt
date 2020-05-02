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

 object robotObserver{
lateinit var client : CoapClient
lateinit var path   : String
			
	fun init( host: String, mypath : String ){  //"coap://localhost:5683/robot/pos"
		path    = mypath
		val url = "$host/$path"
		client =  CoapClient( url )

		GlobalScope.launch{
			println("RESOURCE OBSERVER | STARTS path=$path"  )
			for ( v in 0..10 ) {
				observe( )
				delay(1000)
			}			
 		}
 	} 
	
	fun observe( ){
 			val r = client.get()
			println("RESOURCE OBSERVER |  ${r.getResponseText()}"  ) 
 	}
}

fun main() {
	robotObserver.init("coap://192.168.1.8:5683", "GPIO/17/value")	
	System.`in`.read()
}