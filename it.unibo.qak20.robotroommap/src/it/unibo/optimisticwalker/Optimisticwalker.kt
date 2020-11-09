/* Generated by AN DISI Unibo */ 
package it.unibo.optimisticwalker

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Optimisticwalker ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "waitCmd"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
			var XT = "0"
			var YT = "0"
			var CurrentPlannedMove = ""
			var StepTime    	   = 350L
			var obstacleFound      = false  
		return { //this:ActionBasciFsm
				state("waitCmd") { //this:State
					action { //it:State
						println("&&&  optimisticwalker waits for a command 'movetoCell'")
					}
					 transition(edgeName="t00",targetState="walk",cond=whenRequest("movetoCell"))
				}	 
				state("walk") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("movetoCell(X,Y)"), Term.createTerm("movetoCell(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 XT = payloadArg(0)
											   YT = payloadArg(1)			  
								println("&&&  optimisticwalker  MOVING to ($XT,$YT)")
								itunibo.planner.plannerUtil.planForGoal( "$XT", "$YT"  )
						}
					}
					 transition( edgeName="goto",targetState="execPlannedMoves", cond=doswitch() )
				}	 
				state("execPlannedMoves") { //this:State
					action { //it:State
						  CurrentPlannedMove = itunibo.planner.plannerUtil.getNextPlannedMove()  
					}
					 transition( edgeName="goto",targetState="wMove", cond=doswitchGuarded({ CurrentPlannedMove == "w"  
					}) )
					transition( edgeName="goto",targetState="otherPlannedMove", cond=doswitchGuarded({! ( CurrentPlannedMove == "w"  
					) }) )
				}	 
				state("wMove") { //this:State
					action { //it:State
						request("step", "step($StepTime)" ,"basicrobot" )  
					}
					 transition(edgeName="t01",targetState="stepDone",cond=whenReply("stepdone"))
					transition(edgeName="t02",targetState="stepFailed",cond=whenReply("stepfail"))
				}	 
				state("stepDone") { //this:State
					action { //it:State
						updateResourceRep( itunibo.planner.plannerUtil.getMapOneLine()  
						)
						itunibo.planner.plannerUtil.updateMap( "w"  )
					}
					 transition( edgeName="goto",targetState="execPlannedMoves", cond=doswitchGuarded({ CurrentPlannedMove.length > 0  
					}) )
					transition( edgeName="goto",targetState="sendSuccessAnswer", cond=doswitchGuarded({! ( CurrentPlannedMove.length > 0  
					) }) )
				}	 
				state("stepFailed") { //this:State
					action { //it:State
						 obstacleFound = true  
						println("it.unibo.ctxexplorewawelike | stepFailed")
						if( checkMsgContent( Term.createTerm("stepfail(DURATION,CAUSE)"), Term.createTerm("stepfail(DURATION,CAUSE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var Dt = payloadArg(0).toLong() / 4   
								if(  Dt < 3*StepTime/4.0   
								 ){forward("cmd", "cmd(s)" ,"basicrobot" ) 
								delay(Dt)
								forward("cmd", "cmd(h)" ,"basicrobot" ) 
								}
						}
						itunibo.planner.plannerUtil.updateMapObstacleOnCurrentDirection(  )
					}
					 transition( edgeName="goto",targetState="sendFailureAnswer", cond=doswitch() )
				}	 
				state("otherPlannedMove") { //this:State
					action { //it:State
						if(  CurrentPlannedMove == "l" || CurrentPlannedMove == "r"   
						 ){forward("cmd", "cmd($CurrentPlannedMove)" ,"basicrobot" ) 
						itunibo.planner.plannerUtil.updateMap( "$CurrentPlannedMove"  )
						}
					}
					 transition( edgeName="goto",targetState="execPlannedMoves", cond=doswitch() )
				}	 
				state("sendSuccessAnswer") { //this:State
					action { //it:State
						println("&&&  optimisticwalker POINT ($XT,$YT) REACHED")
						answer("movetoCell", "atcell", "atcell($XT,$YT)"   )  
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("sendFailureAnswer") { //this:State
					action { //it:State
						println("&&&  optimisticwalker FAILS")
						 val Curx = itunibo.planner.plannerUtil.getPosX()
							       val Cury = itunibo.planner.plannerUtil.getPosY()	
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						answer("movetoCell", "walkbreak", "walkbreak($Curx,$Cury)"   )  
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
			}
		}
}
