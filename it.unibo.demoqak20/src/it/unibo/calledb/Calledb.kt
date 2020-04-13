/* Generated by AN DISI Unibo */ 
package it.unibo.calledb

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Calledb ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		var RequestArg = "0" 
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						println("qak2 init")
					}
					 transition(edgeName="t02",targetState="handleRequest",cond=whenRequest("r1"))
				}	 
				state("handleRequest") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("r1(X)"), Term.createTerm("r1(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								RequestArg = payloadArg(0)
								replyreq("r2", "r1", "r2(theta)"   )  
						}
						stateTimer = TimerActor("timer_handleRequest", 
							scope, context!!, "local_tout_calledb_handleRequest", 1000.toLong() )
					}
					 transition(edgeName="t03",targetState="init",cond=whenTimeout("local_tout_calledb_handleRequest"))   
					transition(edgeName="t04",targetState="answerAfterAsk",cond=whenReply("a1"))
				}	 
				state("answerAfterAsk") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("a1(X)"), Term.createTerm("a1(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								val R = RequestArg + payloadArg(0) 
								answer("r1", "a1", "a1($R)"   )  
						}
					}
					 transition( edgeName="goto",targetState="init", cond=doswitch() )
				}	 
			}
		}
}
