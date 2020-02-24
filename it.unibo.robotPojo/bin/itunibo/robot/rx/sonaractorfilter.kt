package itunibo.robot.rx
/*
fcor each (sonar) value V, perfoms some flitering acion
and - if ok - emitLocalStreamEvent   sonarData:sonarData(V)
*/

import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.delay
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct

//TODO : read the linit values from a configuration file
class sonaractorfilter (name : String,  val owner : ActorBasic, 
		var LimitDistance : Int = 7, var LastDistance : Int = 0,
		var minDistance  : Int = 2,   var maxDistance  : Int = 50,
		var maxDelta   : Int   = 1 
) : ApplActorDataStream( name ) {

	init{
		println("   $name |  STARTS")
 	}
 	
	override suspend fun elabData( data : String ){ //
		//println("   $name |  data = $data ")		
		val Distance = Integer.parseInt( data ) 
 		val delta    = Math.abs( Distance - LastDistance )
		var testDelta = delta >= maxDelta  //FOR REAL ROBOT only
 		if( testDelta && Distance > minDistance     ){
 		 	emitLocalStreamEvent( "any", "any($data)" )  //PROPAGATE data to the pipe
			LastDistance = Distance
			if(Distance < LimitDistance){
	 			val m1 = MsgUtil.buildEvent(name, "obstacle", "obstacle($data)")
				println("   ${name} |  emit m1= $m1")
				owner.emit( m1 ) 				//emits the qak event sonarRobot
			}
    	}else{
			//println("   $name |  DISCARDS $Distance ")
 		}				
	}

}