package connQak 
import it.unibo.`is`.interfaces.IObserver
import java.util.Observable
 
enum class ConnectionType {
    TCP, MQTT, COAP, HTTP
}

abstract class connQakBase(val hostIP : String,   val port : String,   val destName : String) {
lateinit var currQakConn  : connQakBase
	
	companion object{
	fun create(connType: ConnectionType,   hostIP : String,   port : String,   destName : String) : connQakBase{
		  showSystemInfo()
		  when( connType ){
				 ConnectionType.MQTT -> 
				 	{return connQakMqtt(hostIP, port, destName)} //; currQakConn.create("ConnQak-MQTT", hostIP, port )
				 ConnectionType.TCP ->
				 	{return connQakTcp(hostIP, port, destName)} //; currQakConn.create("ConnQak-TCP", hostIP, port )
				 ConnectionType.COAP ->
				 	{return connQakCoap(hostIP, port, destName)} //; currQakConn.create("ConnQak-COAP", hostIP, port )
				 ConnectionType.HTTP ->
				 	{return connQakHttp(hostIP, port, destName)} //; currQakConn.create("ConnQak-HTTP", hostIP, port )
// 				 else -> //println("WARNING: protocol unknown")
 		  }
		
	}
	fun showSystemInfo(){
		println(
			"connQakBase  | COMPUTER memory="+ Runtime.getRuntime().totalMemory() +
					" num of processors=" +  Runtime.getRuntime().availableProcessors());
		println(
			"connQakBase  | NUM of threads="+ Thread.activeCount() +
					" currentThread=" + Thread.currentThread() );
	}
	}//object

	
	  abstract fun createConnection( )     
      abstract fun forward( move : String )
      abstract fun request( move : String )
      abstract fun emit( ev : String )
	
}

 
 
 