package rx
/*
 -------------------------------------------------------------------------------------------------
g++  SonarAlone.c -l wiringPi -o  SonarAlone
-------------------------------------------------------------------------------------------------
 */

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import kotlinx.coroutines.runBlocking

object sonarHCSR04Simulator {
 
	val data = sequence<Int>{
		var v0 = 15
		yield(v0)
		while(true){
			v0 = v0 - 3
			yield( v0 )
		}
	}
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun create( actor : ActorBasic  ){
		println("sonarHCSR04Simulator CREATING for ${actor.name}")
 		startDataReadSimulation( actor )
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun startDataReadSimulation( actor: ActorBasic  ){
	
		GlobalScope.launch{
			var i = 0
			while( i < 5 ){
 	 			val m1 = "sonar( ${data.elementAt(i)} )"
				i++
				println("sonarHCSR04Simulator generates $m1")
 				val event = MsgUtil.buildEvent( "sonarHCSR04Simulator","sonarRobot",m1)								
 				actor.emitLocalStreamEvent( event )
				delay( 1000 )
  			}
			
			actor.terminate()
		}
	
		
	}
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
	println("sonarHCSR04Simulator START")
	val qa = sonarDataActorUser("sdau")
	sonarHCSR04Simulator.create( qa )
	qa.waitTermination()
	println("sonarHCSR04Simulator END")
}