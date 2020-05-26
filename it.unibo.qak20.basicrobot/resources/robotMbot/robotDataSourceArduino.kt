package robotMbot
/*
 -------------------------------------------------------------------------------------------------
 Reads data from the serial connection to Arduino
 For each data value V, it emitLocalStreamEvent sonarRobot:sonar(V)
 -------------------------------------------------------------------------------------------------
 */
import it.unibo.kactor.ActorBasicFsm
import kotlinx.coroutines.launch
import java.io.BufferedReader
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct


class  robotDataSourceArduino( name : String, val owner : ActorBasic ,
					val conn : SerialPortConnSupport) : ActorBasic(name, owner.scope){		
	init{
		//scope.launch{  autoMsg("start","start(1)") }
		println("   	%%% $name |  starts conn=$conn")	 
	}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	override suspend fun actorBody(msg: ApplMessage) {
		//println("$tt $name | receives $msg")
 		if( msg.msgId() == "sonarstart"){
			println("$tt $name | STARTING")
			startSensorObserver()
		}
 	}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun startSensorObserver(  ){
         while (true) {
 			try {
 				var curDataFromArduino = conn.receiveALine()
  	 			//println("   	%%% $name | getDataFromArduino received: $curDataFromArduino"    )
 				var v = curDataFromArduino.toDouble() 
   				var dataSonar = v.toInt();													
  				val event = MsgUtil.buildEvent( name,"sonarRobot","sonar( $dataSonar )")								
  				//println("   	%%% $name |  event: ${ event }"   );						
				//owner.emit(  event )
				emitLocalStreamEvent( event )			    
			} catch ( e : Exception) {
 				println("   	%%% $name |  ERROR $e   ")
            }
		}//while
	}
	
 
}