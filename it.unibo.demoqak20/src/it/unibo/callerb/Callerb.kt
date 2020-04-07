/* Generated by AN DISI Unibo */ 
package it.unibo.callerb

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Callerb ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "init"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						println("	qak1 starts")
						request("r1", "r1(10)" ,"calledb" )  
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						println("	qak1 work")
					}
					 transition(edgeName="t00",targetState="handleReply",cond=whenReply("a1"))
					transition(edgeName="t01",targetState="handleAskFromCalled",cond=whenRequest("r2"))
				}	 
				state("handleReply") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
					}
				}	 
				state("handleAskFromCalled") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("r2(X)"), Term.createTerm("r2(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								answer("r2", "a1", "a1(90)"   )  
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}