package unibo.robot
/*
 -------------------------------------------------------------------------------------------------
 A factory that creates the proper support for each specific robot type
 
 NOV 2019:
 The operation create accept as last argument (filter) an ActorBasic to be used
 as a data-stream handler
 
 The operation subscribeToFilter subscribes to the given data-stream handler
 (dsh) another ActorBasic that should work as a data-stream handler
 -------------------------------------------------------------------------------------------------
 */

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ActorBasicFsm
import unibo.robotMock.mockrobotSupport
import org.json.JSONObject
import java.io.File
 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
object robotSupport{
	lateinit var robotKind  :  String
	var endPipehandler      :  ActorBasic? = null 
	
	fun create( owner: ActorBasic, configFileName: String, endPipe: ActorBasic? = null ){
		endPipehandler      =  endPipe
		
		//read Confif.json file
		val config = File("${configFileName}").readText(Charsets.UTF_8)
		//println( "		--- robotSupport | config=$config" )
		val jsonObject   = JSONObject( config )
		robotKind        = jsonObject.getString("type") 
		val robotPort    = jsonObject.getString("port") 
		println( "		--- robotSupport | CREATED for $robotKind port=$robotPort" )

		when( robotKind ){
			"mockrobot"  ->  { unibo.robotMock.mockrobotSupport.create(  ) }
			"virtual"    ->  { unibo.robotVirtual.virtualRobotSupportQak.initClientConn( owner, "localhost", robotPort) }
//			"realmbot"   ->  { itunibo.robotMbot.mbotSupport.create( actor, port  ) }
//			//port="/dev/ttyUSB0"   "COM6"
//			"realnano"   ->  { it.unibo.robotRaspOnly.nanoSupport.create(actor, true ) }
			else -> println( "		--- robotSupport | robot $robotKind unknown" )
		}
	}
	
	fun subscribe( obj : ActorBasic ) : ActorBasic {
		if( endPipehandler != null ) endPipehandler!!.subscribe( obj )
		return obj
	}
	 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun move( cmd : String ){ //cmd = msg(M) M=w | a | s | d | h
//		println("robotSupport move cmd=$cmd robotKind=$robotKind" )
		when( robotKind ){
			"mockrobot"  -> { mockrobotSupport.move( cmd ) 					                  }
			"virtual"    -> { unibo.robotVirtual.virtualRobotSupportQak.doApplMove(  cmd ) 	  }	
//			"realmbot"   -> { itunibo.robotMbot.mbotSupport.move( cmd ) 	}
//			"realnano"   -> { it.unibo.robotRaspOnly.nanoSupport.move( cmd)	}
			else         -> println( "		--- robotSupport | robot unknown")
		}		
	}
	
	fun terminate(){
		when( robotKind ){
			"mockrobot"  -> {  					                  }
			"virtual"    -> { unibo.robotVirtual.virtualRobotSupportQak.terminatevr(  ) 	  }	
//			"realmbot"   -> { itunibo.robotMbot.mbotSupport.move( cmd ) 	}
//			"realnano"   -> { it.unibo.robotRaspOnly.nanoSupport.move( cmd)	}
			else         -> println( "		--- robotSupport | robot unknown")
		}		
		
	}
}