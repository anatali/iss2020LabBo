package robotNano

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage

/* 
 Emits the event sonarRobot : sonar( V )
 */
class sonarHCSR04SupportActor ( name : String ) : ActorBasic( name ) {
companion object {
	val eventId = "sonarRobot"
}		
	lateinit var reader : BufferedReader
	 
	init{
		println("$tt $name | sonarHCSR04SupportActor CREATING")		
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg : ApplMessage){
 		println("$tt $name | received  $msg "  )
		if( msg.msgId() == "sonarstart"){
			println("$tt $name | STARTING")
			try{
				val p = Runtime.getRuntime().exec("sudo ./SonarAlone")
				reader = BufferedReader(  InputStreamReader(p.getInputStream() ))
				startRead(   )
			}catch( e : Exception){
				println("$tt $name | WARNING:  does not find SonarAlone")
			}
 		}
     }
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun startRead(   ){
 		var counter = 0
		GlobalScope.launch{	//to allow message handling
		while( true ){
				var data = reader.readLine()
				//println("sonarHCSR04Support data = $data"   )
				if( data != null ){
					try{
						val v = data.toInt()
						if( v <= 150 ){	//A first filter ...
							val m1 = "sonar( $v )"
							val event = MsgUtil.buildEvent( name,eventId,m1)
							//emit( event )
							emitLocalStreamEvent( event )		//not propagated to remote actors
							println("$tt $name |  ${counter++}: $event "   )
						}
					}catch(e: Exception){}
				}
				delay( 100 ) 
 		}
		}
	}
}