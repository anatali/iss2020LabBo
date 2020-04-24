package rx

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.runBlocking

/*
 -------------------------------------------------------------------------------------------------
 
-------------------------------------------------------------------------------------------------
 */

class sonarSimulator ( name : String ) : ActorBasic( name ) {
  
	val data = sequence<Int>{
		var v0 = 15
		yield(v0)
		while(true){
			v0 = v0 - 1
			yield( v0 )
		}
	}
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg : ApplMessage){
        //println("	--- sonarHCSR04Simulator | received  msg= $msg "  ) 
		println("$tt $name | received  $msg "  )
		startDataReadSimulation(   )
     }
  	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun startDataReadSimulation(    ){
  			var i = 0
			while( i < 7 ){
 	 			val m1 = "sonar( ${data.elementAt(i*2)} )"
				i++
				println("$tt $name | generates $m1")
 				val event = MsgUtil.buildEvent( "sonarHCSR04Simulator","sonarRobot",m1)								
 				emitLocalStreamEvent( event )
 				delay( 1000 )
  			}			
			terminate()
	}

} 

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
 //	val startMsg = MsgUtil.buildDispatch("main","start","start","datasimulator")
	val consumer  = dataConsumer("dataconsumer")
	val simulator = sonarSimulator( "datasimulator" )
	val filter    = dataFilter("datafilter", consumer)
	val logger    = Logger("logger")
	simulator.subscribe( logger ).subscribe( filter ).subscribe( consumer ) 
	MsgUtil.sendMsg("start","start",simulator)
	simulator.waitTermination()
 } 