/* Generated by AN DISI Unibo */ 
package it.unibo.tearoom

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Tearoom ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
		 	val inmapname          = "teaRoomExplored" 
			
			var X_barman		= "0"
			var Y_barman		= "0"
			
			var X_entrancedoor  = "0"
			var Y_entrancedoor  = "0"
			
			var X_exitdoor      = "0"
			var Y_exitdoor      = "0"
			 
			var X_teatable1     = "0"
			var Y_teatable1     = "0"
		
			var X_teatable2     = "0"
			var Y_teatable2     = "0"
			
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						solve("consult('tearoomkb.pl')","") //set resVar	
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
					}
					 transition(edgeName="t00",targetState="evalRequest",cond=whenRequest("getroomstate"))
				}	 
				state("evalRequest") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("getroomstate(V)"), Term.createTerm("getroomstate(numfreetables(N))"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var NumOfFreetables = 0  
								solve("numfreetables(N)","") //set resVar	
								if( currentSolution.isSuccess() ) { NumOfFreetables = getCurSol("N").toString().toInt()   
								println("tearoom | numfreetables=$NumOfFreetables")
								answer("getroomstate", "roomstate", "roomstate(numfreetables($NumOfFreetables))"   )  
								}
								else
								{}
						}
						if( checkMsgContent( Term.createTerm("getroomstate(V)"), Term.createTerm("getroomstate(tablestate(N,S))"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												var t  = Term.createTerm( payloadArg(0) ) as Struct 
												var N  = t.getArg(0).toString().toInt() 
												var S  : String  = ""
								solve("teatable($N,ST)","") //set resVar	
								if( currentSolution.isSuccess() ) { S = getCurSol("ST").toString()    
								println("tearoom | teatable $N state=$S")
								answer("getroomstate", "roomstate", "roomstate(teatable($N,$S))"   )  
								}
								else
								{}
						}
						if( checkMsgContent( Term.createTerm("getroomstate(V)"), Term.createTerm("getroomstate(roomstate(W,T,D))"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												var W  : String  = ""
												var T  : String  = ""
												var D  : String  = ""
								solve("roomstate(W,T,D)","") //set resVar	
								if( currentSolution.isSuccess() ) { W = getCurSol("W").toString()   
												   T = getCurSol("T").toString()
												   D = getCurSol("D").toString()
								println("tearoom | roomstate $W state=$T")
								answer("getroomstate", "roomstate", "roomstate($W,$T,$D)"   )  
								}
								else
								{}
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
			}
		}
}
