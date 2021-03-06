/* Generated by AN DISI Unibo */ 
package it.unibo.radarusage

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Radarusage ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 val ResultMap =   hashMapOf( "key1" to "item1" )  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("demousage start")
					}
					 transition( edgeName="goto",targetState="workUsingDispatch", cond=doswitch() )
				}	 
				state("workUsingDispatch") { //this:State
					action { //it:State
						forward("polar", "polar(0,90)" ,"radargui" ) 
						delay(500) 
						forward("polar", "polar(45,90)" ,"radargui" ) 
						delay(500) 
						forward("polar", "polar(90,90)" ,"radargui" ) 
						delay(500) 
						forward("polar", "polar(120,90)" ,"radargui" ) 
					}
					 transition( edgeName="goto",targetState="workUsingEvents", cond=doswitch() )
				}	 
				state("workUsingRequest") { //this:State
					action { //it:State
						println("radarusage  workUsingRequest")
						utils.sonarDataSimulator.sonarValFromUser( ResultMap, "sonarVal"  )
						 val Result = ResultMap.remove("sonarVal")  
						request("polar", "polar($Result,20)" ,"radargui" )  
						stateTimer = TimerActor("timer_workUsingRequest", 
							scope, context!!, "local_tout_radarusage_workUsingRequest", 1000.toLong() )
					}
					 transition(edgeName="t00",targetState="handleAnwerTimeout",cond=whenTimeout("local_tout_radarusage_workUsingRequest"))   
					transition(edgeName="t01",targetState="handleRadarReply",cond=whenReply("fromRadarGui"))
				}	 
				state("handleAnwerTimeout") { //this:State
					action { //it:State
						println("radarusage WARNING: the radar does not send the answer ... ")
					}
					 transition( edgeName="goto",targetState="workUsingRequest", cond=doswitch() )
				}	 
				state("handleRadarReply") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
					}
					 transition( edgeName="goto",targetState="workUsingRequest", cond=doswitch() )
				}	 
				state("workUsingEvents") { //this:State
					action { //it:State
						println("radarusage  workUsingEvents")
					}
					 transition(edgeName="t02",targetState="handlePolarEvent",cond=whenEvent("polar"))
				}	 
				state("handlePolarEvent") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("polar(D,A)"), Term.createTerm("polar(D,A)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("radarusage handlePolarEvent (${payloadArg(0)},${payloadArg(1)})")
								forward("polar", "polar(${payloadArg(0)},${payloadArg(1)})" ,"radargui" ) 
						}
					}
					 transition( edgeName="goto",targetState="workUsingEvents", cond=doswitch() )
				}	 
			}
		}
}
