/* Generated by AN DISI Unibo */ 
package it.unibo.virtualrobot

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Virtualrobot ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("virtualrobot | START")
						robotVirtual.virtualrobotSupport.create(myself ,"192.168.1.22", "8999" )
						delay(500) 
						robotVirtual.virtualrobotSupport.move( "l"  )
						delay(500) 
						robotVirtual.virtualrobotSupport.move( "r"  )
						updateResourceRep( "stopped"  
						)
						  
						 			var robotsonar = context!!.hasActor("robotsonar")  
						 			if( robotsonar != null ){ 
						  				//ACTIVATE THE DATA SOURCE realsonar
						 				forward("sonarstart", "sonarstart(1)" ,"robotsonar" ) 	 			
						 				//SET THE PIPE
						 				robotsonar.
						 				subscribeLocalActor("datacleaner").
						 				subscribeLocalActor("distancefilter").
						 				subscribeLocalActor("virtualrobot")		//in order to perceive obstacle
						 			}else{
						 				println("virtualrobot | WARNING: robotsonar NOT FOUND")
						 			}
						discardMessages = false
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
					}
					 transition(edgeName="t10",targetState="execcmd",cond=whenDispatch("cmd"))
					transition(edgeName="t11",targetState="handleObstacle",cond=whenEvent("obstacle"))
					transition(edgeName="t12",targetState="handleSonar",cond=whenEvent("sonar"))
				}	 
				state("execcmd") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("cmd(X)"), Term.createTerm("cmd(MOVE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								updateResourceRep( "move(${payloadArg(0)})"  
								)
								robotVirtual.virtualrobotSupport.move( payloadArg(0)  )
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("handleObstacle") { //this:State
					action { //it:State
						println("virtualrobot | handleObstacle (todo: look at direction)")
						robotVirtual.virtualrobotSupport.move( "h"  )
						robotVirtual.virtualrobotSupport.move( "l"  )
						robotVirtual.virtualrobotSupport.move( "l"  )
						robotVirtual.virtualrobotSupport.move( "w"  )
						delay(100) 
						robotVirtual.virtualrobotSupport.move( "h"  )
						robotVirtual.virtualrobotSupport.move( "l"  )
						robotVirtual.virtualrobotSupport.move( "l"  )
						if( checkMsgContent( Term.createTerm("obstacle(D)"), Term.createTerm("obstacle(TARGET)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								updateResourceRep( "obstacle"  
								)
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("handleSonar") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}
